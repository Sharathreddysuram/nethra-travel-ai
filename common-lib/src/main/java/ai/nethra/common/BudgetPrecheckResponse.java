package ai.nethra.common;
import java.math.BigDecimal; import java.util.List;
public record BudgetPrecheckResponse(boolean plausible, BigDecimal totalBudget, BigDecimal spendableBudget, BigDecimal spendablePerDay, BudgetAllocationResponse allocation, List<String> warnings) {}
