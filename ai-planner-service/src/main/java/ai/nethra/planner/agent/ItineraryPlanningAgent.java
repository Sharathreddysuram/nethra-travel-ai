package ai.nethra.planner.agent;
import ai.nethra.common.*; import ai.nethra.planner.client.ItineraryClient; import ai.nethra.planner.graph.TravelGraphState; import org.springframework.stereotype.Component;
@Component public class ItineraryPlanningAgent {
  private final ItineraryClient itinerary; public ItineraryPlanningAgent(ItineraryClient itinerary){this.itinerary=itinerary;}
  public void execute(TravelGraphState s){ s.itinerary=itinerary.plan(new ItineraryRequest(s.destination.name(),s.request.daysValue(),s.activities,s.request.transportMode())); s.addTrace("ItineraryPlanningAgent","PLANNED","Day-by-day itinerary created for " + s.itinerary.days().size() + " day(s); arrival and departure days are kept lighter."); }
}
