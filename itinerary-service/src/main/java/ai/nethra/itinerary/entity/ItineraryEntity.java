package ai.nethra.itinerary.entity;
import jakarta.persistence.*;
@Entity @Table(name="itinerarys")
public class ItineraryEntity {
  @Id private String id;
  private String destinationName;
  private Integer days;
  private java.math.BigDecimal totalActivityCost;
  private java.time.Instant createdAt;
  public ItineraryEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
