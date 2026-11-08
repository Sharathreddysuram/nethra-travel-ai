package ai.nethra.common;
import java.math.BigDecimal; import java.math.RoundingMode; import java.util.*;
public final class TripCostEstimator {
  private TripCostEstimator() {}
  public static BigDecimal foodCost(DestinationProfile d, int days, int travelers){ return Moneys.round(d.foodPerDay().multiply(BigDecimal.valueOf((long)days*travelers))); }
  public static BigDecimal transportCost(String source, DestinationProfile d, TransportMode mode, int travelers){ BigDecimal unit=TravelCatalog.transportUnitPrice(source,d,mode); return mode==TransportMode.CAR ? unit : Moneys.round(unit.multiply(BigDecimal.valueOf(travelers))); }
  public static BigDecimal minimumActivityCost(DestinationProfile d, int days, int travelers){ List<ActivityItem> sorted=ActivityCatalog.forDestination(d.id()).stream().sorted(Comparator.comparing(ActivityItem::price)).limit(Math.min(Math.max(days,1),3)).toList(); return Moneys.round(sorted.stream().map(ActivityItem::price).reduce(BigDecimal.ZERO,BigDecimal::add).multiply(BigDecimal.valueOf(travelers))); }
  public static BigDecimal separateLodgingCost(DestinationProfile d, int days, int travelers){ int nights=Math.max(0,days-1); int rooms=(travelers+1)/2; return Moneys.round(d.hotelBasePerNight().multiply(new BigDecimal("0.80")).multiply(BigDecimal.valueOf((long)nights*rooms))); }
  public static DestinationMinimum estimate(String source, DestinationProfile d, TransportMode mode, int days, int travelers){
    BigDecimal transport=transportCost(source,d,mode,travelers); BigDecimal food=foodCost(d,days,travelers);
    BigDecimal separate=separateLodgingCost(d,days,travelers).add(minimumActivityCost(d,days,travelers));
    BigDecimal lodgingActivity=separate; boolean partner=false;
    List<PartnerPackage> packs=PartnerCatalog.forDestination(d.id()); if(!packs.isEmpty()){ BigDecimal q=packs.get(0).quote(days,travelers); partner=true; if(q.compareTo(lodgingActivity)<0) lodgingActivity=q; }
    BigDecimal subtotal=transport.add(food).add(lodgingActivity);
    BigDecimal required=subtotal.divide(new BigDecimal("0.92"),0,RoundingMode.CEILING);
    return new DestinationMinimum(d.id(),d.name(),mode,Moneys.round(transport),Moneys.round(lodgingActivity),Moneys.round(food),Moneys.round(subtotal),Moneys.round(required),partner);
  }
  public static List<DestinationMinimum> allMinimums(String source, int days, int travelers, TransportMode onlyMode){
    List<DestinationMinimum> out=new ArrayList<>(); for(DestinationProfile d: TravelCatalog.destinations()){ if(TravelCatalog.cityKey(source).equals(TravelCatalog.cityKey(d.name()))) continue; for(TransportMode m: TransportMode.values()){ if(onlyMode==null||onlyMode==m) out.add(estimate(source,d,m,days,travelers)); } } return out;
  }
}
