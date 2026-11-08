package ai.nethra.common;
import java.util.List;
public record ItineraryRequest(String destinationName, Integer days, List<ActivityItem> activities, TransportMode transportMode) {}
