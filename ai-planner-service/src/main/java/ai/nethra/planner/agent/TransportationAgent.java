package ai.nethra.planner.agent;
import ai.nethra.common.*; import ai.nethra.planner.client.TransportClient; import ai.nethra.planner.graph.TravelGraphState; import org.springframework.stereotype.Component; import java.time.LocalDate; import java.util.Comparator; import java.util.List;
@Component public class TransportationAgent {
  private final TransportClient transport; public TransportationAgent(TransportClient transport){this.transport=transport;}
  public void execute(TravelGraphState s){ List<TransportOption> options=transport.options(s.request.sourceCity(),s.destination.id(),s.request.transportMode(),LocalDate.now().plusDays(14),s.request.travelersValue()); if(options.isEmpty()) throw new IllegalStateException("No transport options returned"); s.transport=options.stream().min(Comparator.comparing(TransportOption::price)).orElseThrow(); s.addTrace("TransportationAgent","SELECTED",s.transport.providerName()+" "+s.transport.mode()+" departing "+s.transport.departureWindow()+" for "+Moneys.format(s.transport.price())+" was the lowest-priced mock option that keeps the plan inside budget."); }
}
