package ai.nethra.common;
import java.time.Instant;
public record NotificationResponse(String id, String recipient, String channel, String subject, String status, Instant createdAt) {}
