package ai.nethra.planner.graph;
import ai.nethra.common.*; import ai.nethra.planner.agent.*; import ai.nethra.planner.llm.LlmClient; import org.springframework.stereotype.Component; import java.util.List;
@Component public class TravelGraph {
  private final BudgetAnalysisAgent budget; private final FeasibilityAgent feasibility; private final DestinationSelectionAgent destination; private final TransportationAgent transport; private final HotelSelectionAgent hotel; private final ActivitySelectionAgent activity; private final ItineraryPlanningAgent itinerary; private final BudgetValidationAgent validation; private final LlmClient llm;
  public TravelGraph(BudgetAnalysisAgent budget,FeasibilityAgent feasibility,DestinationSelectionAgent destination,TransportationAgent transport,HotelSelectionAgent hotel,ActivitySelectionAgent activity,ItineraryPlanningAgent itinerary,BudgetValidationAgent validation,LlmClient llm){this.budget=budget;this.feasibility=feasibility;this.destination=destination;this.transport=transport;this.hotel=hotel;this.activity=activity;this.itinerary=itinerary;this.validation=validation;this.llm=llm;}
  public TravelPlanResponse run(PlanRequest request){
    TravelGraphState s=new TravelGraphState(request);
    budget.execute(s);
    if(!feasibility.execute(s)) return validation.buildRejection(s);
    destination.loadCandidates(s);
    for(int i=0;i<s.ranked.size();i++){ s.resetCandidate(); destination.select(s,i); transport.execute(s); hotel.execute(s); activity.execute(s); itinerary.execute(s); if(validation.validate(s)){ String summary=llm.summarize(s); return new TravelPlanResponse(true,request,s.destination,s.transport,s.hotel,s.partnerPackage,List.copyOf(s.activities),s.itinerary,s.breakdown,s.totalCost,request.budgetInr().subtract(s.totalCost),summary,s.guideHighlights,List.copyOf(s.trace),null,null,List.of(),List.of(),List.of()); } }
    throw new IllegalStateException("Feasibility passed but no ranked destination produced a within-budget plan");
  }
}
