package ai.nethra.common;
import java.math.BigDecimal;
public record PlanRequest(BigDecimal budgetInr, String sourceCity, Integer days, TransportMode transportMode, Integer travelers) {
  public PlanRequest {
    if (budgetInr == null || budgetInr.signum() <= 0) throw new IllegalArgumentException("budgetInr must be positive");
    if (sourceCity == null || sourceCity.isBlank()) throw new IllegalArgumentException("sourceCity is required");
    if (days == null || days < 1 || days > 30) throw new IllegalArgumentException("days must be between 1 and 30");
    if (transportMode == null) transportMode = TransportMode.TRAIN;
    if (travelers == null || travelers < 1) travelers = 1;
    if (travelers > 20) throw new IllegalArgumentException("travelers must be 20 or fewer");
  }
  public int daysValue() { return days; }
  public int travelersValue() { return travelers; }
  public int nights() { return Math.max(0, days - 1); }
  public int rooms() { return (travelers + 1) / 2; }
  public BigDecimal emergencyBuffer() { return Moneys.percent(budgetInr, 8); }
}
