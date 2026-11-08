package ai.nethra.activity.provider;
import ai.nethra.common.*; import org.springframework.stereotype.Component; import java.util.List;
@Component public class MockActivityProvider implements ActivityProvider { public List<ActivityItem> activities(String destinationId){ String id=TravelCatalog.find(destinationId).map(DestinationProfile::id).orElse(destinationId); return ActivityCatalog.forDestination(id); } }
