package ai.nethra.planner.client;
import ai.nethra.common.*; import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*;
@FeignClient(name="notification-service", url="${services.notification.url:http://localhost:8090}") public interface NotificationClient {
  @PostMapping("/api/notifications") NotificationResponse send(@RequestBody NotificationRequest request);
}
