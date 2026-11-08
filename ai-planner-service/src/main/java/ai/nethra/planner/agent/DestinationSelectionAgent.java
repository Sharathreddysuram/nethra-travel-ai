package ai.nethra.planner.agent;
import ai.nethra.common.*; import ai.nethra.planner.client.DestinationClient; import ai.nethra.planner.graph.TravelGraphState; import ai.nethra.planner.rag.RagService; import org.springframework.stereotype.Component;
@Component public class DestinationSelectionAgent {
  private final DestinationClient destinations; private final RagService rag;
  public DestinationSelectionAgent(DestinationClient destinations,RagService rag){this.destinations=destinations;this.rag=rag;}
  public void loadCandidates(TravelGraphState s){ s.ranked=destinations.rank(s.request.sourceCity(),s.request.transportMode(),s.request.budgetInr(),s.request.daysValue(),s.request.travelersValue()); if(s.ranked.isEmpty()) throw new IllegalStateException("Destination service returned no candidates"); s.addTrace("DestinationSelectionAgent","RANKED",s.ranked.size()+" destinations scored by value-for-money, budget fit, transport cost, rating and days fit. Top candidate: " + s.ranked.get(0).destination().name() + " (" + s.ranked.get(0).score() + ")."); }
  public void select(TravelGraphState s,int index){ DestinationRank rank=s.ranked.get(index); s.selectedRank=rank; s.destination=rank.destination(); s.guideHighlights=rag.highlights(s.destination,s.request,3); s.addTrace("DestinationSelectionAgent","SELECTED",s.destination.name() + " selected: " + rank.rationale() + " Guide evidence: " + String.join(" ", s.guideHighlights)); }
}
