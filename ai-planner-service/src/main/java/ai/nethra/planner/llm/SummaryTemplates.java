package ai.nethra.planner.llm;
import ai.nethra.common.Moneys; import ai.nethra.planner.graph.TravelGraphState;
public final class SummaryTemplates { private SummaryTemplates(){}
  public static String summarize(TravelGraphState s){ String stay=s.partnerPackage!=null ? s.partnerPackage.partnerPackage().name() : s.hotel!=null ? s.hotel.name() : "a day-trip plan with no overnight stay"; return "Nethra planned a " + s.request.daysValue() + "-day trip from " + s.request.sourceCity() + " to " + s.destination.name() + " by " + s.request.transportMode() + " for " + Moneys.format(s.totalCost) + ", inside your " + Moneys.format(s.request.budgetInr()) + " budget. Stay and experiences are anchored by " + stay + ", with " + s.activities.size() + " additional paid activities, a protected emergency buffer of " + Moneys.format(s.buffer()) + ", and " + Moneys.format(s.request.budgetInr().subtract(s.totalCost)) + " left unspent. Where you can't see, we help you see."; }
}
