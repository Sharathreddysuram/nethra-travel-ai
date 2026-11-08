package ai.nethra.itinerary.service;
import ai.nethra.common.*; import org.springframework.stereotype.Service; import java.math.BigDecimal; import java.util.*;
@Service public class ItineraryService {
  public ItineraryPlan plan(ItineraryRequest req){ int days=req.days()==null?1:req.days(); List<ActivityItem> acts=new ArrayList<>(req.activities()==null?List.of():req.activities()); acts.sort(Comparator.comparingInt((ActivityItem a)->slotRank(a.preferredSlot())).thenComparing(ActivityItem::durationHours));
    List<List<ActivityItem>> byDay=new ArrayList<>(); for(int i=0;i<days;i++) byDay.add(new ArrayList<>());
    for(ActivityItem a: acts){ int target=-1; for(int d=0;d<days;d++){ if(byDay.get(d).size()<capacity(d,days)){ target=d; break; } } if(target>=0) byDay.get(target).add(a); }
    List<DayPlan> out=new ArrayList<>(); BigDecimal total=BigDecimal.ZERO;
    for(int d=0;d<days;d++){ List<DayPlan.ItinerarySlot> slots=new ArrayList<>(); List<ActivityItem> dayActs=byDay.get(d); for(int i=0;i<dayActs.size();i++){ ActivityItem a=dayActs.get(i); slots.add(new DayPlan.ItinerarySlot(timeFor(i),a.name(),a.description(),a.price())); total=total.add(a.price()); }
      if(slots.size()<capacity(d,days)){ if(d==0) slots.add(new DayPlan.ItinerarySlot("16:30","Arrive, settle in and explore nearby","Keep arrival day light: check in, exchange money if needed and take a short neighbourhood walk.",BigDecimal.ZERO)); else if(d==days-1&&days>1) slots.add(new DayPlan.ItinerarySlot("15:00","Souvenirs and departure buffer","Pack, pick up local specialties and leave generous time for the return journey.",BigDecimal.ZERO)); else slots.add(new DayPlan.ItinerarySlot("16:30","Free exploration","Unscheduled time for cafes, markets or rest — often where the best trip moments happen.",BigDecimal.ZERO)); }
      String title=d==0?"Arrival in "+req.destinationName() : d==days-1&&days>1 ? "Departure from "+req.destinationName() : "Best of "+req.destinationName()+" — day "+(d+1);
      BigDecimal dayCost=dayActs.stream().map(ActivityItem::price).reduce(BigDecimal.ZERO,BigDecimal::add); out.add(new DayPlan(d+1,title,slots,Moneys.round(dayCost))); }
    return new ItineraryPlan(req.destinationName(),out,Moneys.round(total)); }
  private int capacity(int day,int days){ if(days==1) return 3; if(day==0||day==days-1) return 2; return 3; }
  private int slotRank(String slot){ return switch(slot==null?"":slot){ case "MORNING" -> 0; case "AFTERNOON" -> 1; case "EVENING" -> 2; default -> 1; }; }
  private String timeFor(int idx){ return switch(idx){ case 0 -> "09:00"; case 1 -> "14:00"; default -> "18:30"; }; }
}
