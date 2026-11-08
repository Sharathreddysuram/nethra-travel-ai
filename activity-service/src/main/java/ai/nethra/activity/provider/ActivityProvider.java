package ai.nethra.activity.provider;
import ai.nethra.common.ActivityItem; import java.util.List;
public interface ActivityProvider { List<ActivityItem> activities(String destinationId); }
