package ai.nethra.notification.controller;
import ai.nethra.common.*; import ai.nethra.notification.service.NotificationService; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/notifications") public class NotificationController {
  private final NotificationService service; public NotificationController(NotificationService service){this.service=service;}
  @PostMapping public NotificationResponse send(@RequestBody NotificationRequest req){ return service.send(req); }
  @GetMapping("/history") public List<NotificationResponse> history(){ return service.history(); }
}
