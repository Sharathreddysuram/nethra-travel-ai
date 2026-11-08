package ai.nethra.common;
import java.math.BigDecimal; import java.util.List;
public record DayPlan(int dayNumber, String title, List<ItinerarySlot> slots, BigDecimal estimatedActivityCost) {
  public record ItinerarySlot(String timeLabel, String activityName, String description, BigDecimal cost) {}
}
