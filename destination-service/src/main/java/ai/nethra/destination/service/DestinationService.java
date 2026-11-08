package ai.nethra.destination.service;
import ai.nethra.common.*; import org.springframework.stereotype.Service; import java.math.BigDecimal; import java.util.*;
@Service public class DestinationService {
  public List<DestinationProfile> all(){ return TravelCatalog.destinations(); }
  public DestinationProfile get(String id){ return TravelCatalog.find(id).orElseThrow(() -> new IllegalArgumentException("Unknown destination: " + id)); }
  public List<DestinationMinimum> minimums(String source, int days, int travelers, TransportMode mode){ return TripCostEstimator.allMinimums(source, days, travelers, mode); }
  public List<DestinationRank> rank(String source, TransportMode mode, BigDecimal budget, int days, int travelers){
    List<DestinationRank> out=new ArrayList<>(); BigDecimal spendable=budget.multiply(new BigDecimal("0.92"));
    for(DestinationProfile d: TravelCatalog.destinations()){ if(TravelCatalog.cityKey(source).equals(TravelCatalog.cityKey(d.name()))) continue; DestinationMinimum est=TripCostEstimator.estimate(source,d,mode,days,travelers);
      double ratingScore=d.rating().doubleValue()*8.0;
      double valueScore=clamp(25.0*(1.0-est.minimumRequiredBudget().doubleValue()/(budget.doubleValue()*1.75)),0,25);
      double transportScore=clamp(15.0*(1.0-est.transportCost().doubleValue()/(budget.doubleValue()*0.9)),0,15);
      int dayPenalty=days<d.idealDaysMin()? d.idealDaysMin()-days : days>d.idealDaysMax()? days-d.idealDaysMax():0; double daysScore=Math.max(0,20-dayPenalty*4);
      double fitBonus=est.minimumRequiredBudget().compareTo(spendable)<=0 ? 0 : -10;
      BigDecimal score=BigDecimal.valueOf(Math.max(0,Math.min(100,ratingScore+valueScore+transportScore+daysScore+fitBonus))).setScale(2,java.math.RoundingMode.HALF_UP);
      String rationale=String.format(Locale.ROOT,"%s scores %.1f: rating %.1f, minimum %s for %s, ideal for %d-%d days.",d.name(),score.doubleValue(),d.rating().doubleValue(),Moneys.format(est.minimumRequiredBudget()),mode,d.idealDaysMin(),d.idealDaysMax());
      out.add(new DestinationRank(d,score,est.minimumRequiredBudget(),est.transportCost(),rationale)); }
    out.sort(Comparator.comparing(DestinationRank::score).reversed()); return out;
  }
  private double clamp(double v,double min,double max){ return Math.max(min,Math.min(max,v)); }
}
