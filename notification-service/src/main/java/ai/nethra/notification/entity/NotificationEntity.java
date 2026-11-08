package ai.nethra.notification.entity;
import jakarta.persistence.*;
@Entity @Table(name="notifications")
public class NotificationEntity {
  @Id private String id;
  private String recipient;
  private String channel;
  private String subject;
  private String status;
  private java.time.Instant createdAt;
  public NotificationEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
