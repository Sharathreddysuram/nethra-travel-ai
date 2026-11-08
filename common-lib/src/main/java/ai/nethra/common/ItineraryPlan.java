package ai.nethra.common;
import java.math.BigDecimal; import java.util.List;
public record ItineraryPlan(String destinationName, List<DayPlan> days, BigDecimal totalActivityCost) {}
