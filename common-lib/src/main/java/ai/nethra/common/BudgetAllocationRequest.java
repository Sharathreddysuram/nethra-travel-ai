package ai.nethra.common;
import java.math.BigDecimal;
public record BudgetAllocationRequest(BigDecimal budgetInr, Integer days, TransportMode transportMode, Integer travelers) {
  public PlanRequest toPlanRequest(String sourceCity) { return new PlanRequest(budgetInr, sourceCity, days, transportMode, travelers); }
}
