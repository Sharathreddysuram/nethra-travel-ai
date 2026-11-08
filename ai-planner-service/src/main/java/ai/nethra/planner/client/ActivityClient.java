package ai.nethra.planner.client;
import ai.nethra.common.ActivityItem; import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*; import java.util.List;
@FeignClient(name="activity-service", url="${services.activity.url:http://localhost:8086}") public interface ActivityClient {
  @GetMapping("/api/activities") List<ActivityItem> activities(@RequestParam("destinationId") String destinationId);
}
