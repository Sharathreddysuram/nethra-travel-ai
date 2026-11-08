package ai.nethra.activity.entity;
import jakarta.persistence.*;
@Entity @Table(name="activitys")
public class ActivityEntity {
  @Id private String id;
  private String destinationId;
  private String name;
  private java.math.BigDecimal price;
  private java.math.BigDecimal durationHours;
  private String category;
  public ActivityEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
