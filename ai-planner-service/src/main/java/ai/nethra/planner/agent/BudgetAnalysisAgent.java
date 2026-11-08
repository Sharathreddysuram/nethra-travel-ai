package ai.nethra.planner.agent;
import ai.nethra.common.*; import ai.nethra.planner.client.BudgetClient; import ai.nethra.planner.graph.TravelGraphState; import org.springframework.stereotype.Component;
@Component public class BudgetAnalysisAgent {
  private final BudgetClient budget; public BudgetAnalysisAgent(BudgetClient budget){this.budget=budget;}
  public void execute(TravelGraphState s){ s.allocation=budget.allocate(new BudgetAllocationRequest(s.request.budgetInr(), s.request.daysValue(), s.request.transportMode(), s.request.travelersValue())); s.addTrace("BudgetAnalysisAgent","ALLOCATED","Budget " + Moneys.format(s.request.budgetInr()) + " split across transport, hotel, food and activities; " + Moneys.format(s.allocation.emergencyBuffer()) + " (8%) held back as emergency buffer."); }
}
