package ai.nethra.transport.provider;
import ai.nethra.common.*; import java.math.BigDecimal; import java.math.RoundingMode; import java.time.LocalDate; import java.util.*;
public abstract class AbstractTransportProvider implements TransportProvider {
  protected abstract String providerName(); protected abstract List<String[]> windows();
  public List<TransportOption> options(String sourceCity, String destinationId, LocalDate travelDate, int travelers){
    DestinationProfile d=TravelCatalog.find(destinationId).orElseThrow(() -> new IllegalArgumentException("Unknown destination: " + destinationId)); LocalDate date=travelDate==null?LocalDate.now().plusDays(14):travelDate;
    BigDecimal base=TravelCatalog.transportUnitPrice(sourceCity,d,mode()); double air=TravelCatalog.airDistanceKm(sourceCity,d); double road=TravelCatalog.roadDistanceKm(sourceCity,d);
    double duration = switch(mode()){ case FLIGHT -> air/720.0+1.2; case TRAIN -> road/58.0+1.0; case BUS -> road/42.0+1.0; case CAR -> road/48.0+1.0; };
    int hash=Math.floorMod((date+"|"+mode()+"|"+d.id()).hashCode(),8); double dateFactor=0.97+hash*0.01;
    List<TransportOption> out=new ArrayList<>(); List<String[]> specs=windows();
    for(int i=0;i<specs.size();i++){ double mult=Double.parseDouble(specs.get(i)[1]); BigDecimal unit=base.multiply(BigDecimal.valueOf(mult*dateFactor)); BigDecimal price=mode()==TransportMode.CAR? Moneys.rupees(unit) : Moneys.rupees(unit.multiply(BigDecimal.valueOf(travelers))); BigDecimal dur=BigDecimal.valueOf(duration+(i*0.35)).setScale(1,RoundingMode.HALF_UP); BigDecimal score=BigDecimal.valueOf(Math.max(45,94-(i*7)-(mult-1)*40)).setScale(1,RoundingMode.HALF_UP); out.add(new TransportOption(mode().name().toLowerCase()+"-"+d.id()+"-"+(i+1),mode(),providerName(),sourceCity,d.name(),date,specs.get(i)[0],price,dur,i==1,score)); }
    out.sort(Comparator.comparing(TransportOption::valueScore).reversed()); return out;
  }
}
