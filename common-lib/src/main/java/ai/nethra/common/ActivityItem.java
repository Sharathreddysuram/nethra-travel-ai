package ai.nethra.common;
import java.math.BigDecimal;
public record ActivityItem(String id, String destinationId, String name, String description, BigDecimal price, BigDecimal durationHours, String category, String preferredSlot) {}
