package ai.nethra.notification.service;
import ai.nethra.common.*; import org.slf4j.*; import org.springframework.stereotype.Service; import java.time.Instant; import java.util.*; import java.util.concurrent.CopyOnWriteArrayList;
@Service public class NotificationService {
  private static final Logger log=LoggerFactory.getLogger(NotificationService.class); private final List<NotificationResponse> history=new CopyOnWriteArrayList<>();
  public NotificationResponse send(NotificationRequest req){ String channel=req.channel()==null?"EMAIL":req.channel().toUpperCase(Locale.ROOT); NotificationResponse r=new NotificationResponse(UUID.randomUUID().toString(),req.recipient(),channel,req.subject(),"LOGGED_BY_MOCK_SENDER",Instant.now()); history.add(0,r); log.info("[MOCK NOTIFICATION] channel={} to={} subject={} ref={}",channel,req.recipient(),req.subject(),req.referenceId()); return r; }
  public List<NotificationResponse> history(){ return List.copyOf(history); }
}
