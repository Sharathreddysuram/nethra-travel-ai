package ai.nethra.planner.agent;
import ai.nethra.common.*; import ai.nethra.planner.client.ActivityClient; import ai.nethra.planner.graph.TravelGraphState; import org.springframework.stereotype.Component; import java.math.BigDecimal; import java.util.*;
@Component public class ActivitySelectionAgent {
  private final ActivityClient activities; public ActivitySelectionAgent(ActivityClient activities){this.activities=activities;}
  public void execute(TravelGraphState s){
    BigDecimal cap=s.request.budgetInr().subtract(s.buffer()).subtract(s.transport.price()).subtract(s.foodCost()).subtract(s.lodgingCost()); if(cap.signum()<0) cap=BigDecimal.ZERO;
    List<ActivityItem> all=new ArrayList<>(activities.activities(s.destination.id())); all.sort(Comparator.comparing(ActivityItem::price));
    int maxCount=s.partnerPackage!=null ? s.request.daysValue() : s.request.daysValue()*2; BigDecimal spent=BigDecimal.ZERO; Set<String> categories=new HashSet<>();
    // First pass: one activity per category for variety, cheapest first.
    for(ActivityItem a: all){ if(s.activities.size()>=maxCount) break; BigDecimal cost=a.price().multiply(BigDecimal.valueOf(s.request.travelersValue())); if(!categories.contains(a.category()) && spent.add(cost).compareTo(cap)<=0){ s.activities.add(a); categories.add(a.category()); spent=spent.add(cost); } }
    // Second pass: fill remaining activity budget with the cheapest remaining experiences.
    for(ActivityItem a: all){ if(s.activities.size()>=maxCount) break; if(s.activities.contains(a)) continue; BigDecimal cost=a.price().multiply(BigDecimal.valueOf(s.request.travelersValue())); if(spent.add(cost).compareTo(cap)<=0){ s.activities.add(a); spent=spent.add(cost); } }
    s.addTrace("ActivitySelectionAgent", s.partnerPackage!=null ? "SUPPLEMENTED_PACKAGE" : "SELECTED", s.activities.size()+" paid activities selected for " + Moneys.format(spent) + " within the remaining activity cap of " + Moneys.format(cap) + (s.partnerPackage!=null ? "; the partner package's included tours remain the core experiences." : "."));
  }
}
