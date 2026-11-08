package ai.nethra.planner.agent;
import ai.nethra.common.*; import ai.nethra.planner.client.DestinationClient; import ai.nethra.planner.graph.TravelGraphState; import org.springframework.stereotype.Component; import java.math.BigDecimal; import java.util.*;
@Component public class FeasibilityAgent {
  private final DestinationClient destinations; public FeasibilityAgent(DestinationClient destinations){this.destinations=destinations;}
  public boolean execute(TravelGraphState s){
    s.minimums=destinations.minimums(s.request.sourceCity(), s.request.daysValue(), s.request.travelersValue(), null);
    List<DestinationMinimum> requested=s.minimums.stream().filter(m->m.transportMode()==s.request.transportMode()).toList();
    if(requested.isEmpty()) throw new IllegalStateException("No minimum estimates for mode " + s.request.transportMode());
    DestinationMinimum cheapest=requested.stream().min(Comparator.comparing(DestinationMinimum::minimumRequiredBudget)).orElseThrow();
    if(cheapest.minimumRequiredBudget().compareTo(s.request.budgetInr())<=0){ s.addTrace("FeasibilityAgent","FEASIBLE","Cheapest true minimum for " + s.request.transportMode() + " is " + Moneys.format(cheapest.minimumRequiredBudget()) + " to " + cheapest.destinationName() + ", within the " + Moneys.format(s.request.budgetInr()) + " budget after the 8% buffer."); return true; }
    s.rejectionMinimum=cheapest.minimumRequiredBudget();
    s.rejectionReason="The cheapest possible " + s.request.transportMode() + " trip from " + s.request.sourceCity() + " for " + s.request.daysValue() + " day(s) is to " + cheapest.destinationName() + " and needs at least " + Moneys.format(cheapest.minimumRequiredBudget()) + " once transport, the lowest honest hotel/activity costs, food and the 8% emergency buffer are included. That is above the requested budget of " + Moneys.format(s.request.budgetInr()) + ", so no itinerary was generated.";
    Map<TransportMode, DestinationMinimum> bestByMode=new EnumMap<>(TransportMode.class);
    for(DestinationMinimum m: s.minimums){ bestByMode.merge(m.transportMode(), m, (a,b)->a.minimumRequiredBudget().compareTo(b.minimumRequiredBudget())<=0?a:b); }
    List<ModeMinimum> modes=new ArrayList<>(); bestByMode.forEach((mode,m)->{ if(mode!=s.request.transportMode()) modes.add(new ModeMinimum(mode,m.destinationName(),m.minimumRequiredBudget())); }); modes.sort(Comparator.comparing(ModeMinimum::minimumRequiredBudget)); s.rejectionModes=modes;
    s.rejectionDestinations=requested.stream().sorted(Comparator.comparing(DestinationMinimum::minimumRequiredBudget)).limit(3).toList();
    List<String> suggestions=new ArrayList<>();
    if(!modes.isEmpty()) suggestions.add("Switch to " + modes.get(0).transportMode() + ": the cheapest trip (to " + modes.get(0).destinationName() + ") needs about " + Moneys.format(modes.get(0).minimumRequiredBudget()) + ".");
    if(s.request.daysValue()>1){ try { List<DestinationMinimum> shorter=destinations.minimums(s.request.sourceCity(), s.request.daysValue()-1, s.request.travelersValue(), s.request.transportMode()); shorter.stream().min(Comparator.comparing(DestinationMinimum::minimumRequiredBudget)).ifPresent(m->suggestions.add("Shorten the trip to " + (s.request.daysValue()-1) + " day(s): the " + s.request.transportMode() + " minimum falls to about " + Moneys.format(m.minimumRequiredBudget()) + " (" + m.destinationName() + ").")); } catch(Exception ignored) {} }
    suggestions.add("Increase the budget to at least " + Moneys.format(cheapest.minimumRequiredBudget()) + " to keep " + s.request.transportMode() + " for " + s.request.daysValue() + " day(s).");
    suggestions.add("The lowest-cost destination for your selected mode is " + cheapest.destinationName() + " at " + Moneys.format(cheapest.minimumRequiredBudget()) + ".");
    s.rejectionSuggestions=suggestions;
    s.addTrace("FeasibilityAgent","INFEASIBLE","Minimum required " + Moneys.format(cheapest.minimumRequiredBudget()) + " exceeds budget " + Moneys.format(s.request.budgetInr()) + "; planning stopped before destination, hotel or itinerary generation.");
    return false;
  }
}
