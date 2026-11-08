package ai.nethra.common;
import java.math.BigDecimal; import java.util.List; import java.util.Map;
public record BudgetAllocationResponse(BigDecimal totalBudget, BigDecimal spendableBudget, BigDecimal emergencyBuffer, Map<BudgetCategory, BigDecimal> allocations, List<String> assumptions) {}
