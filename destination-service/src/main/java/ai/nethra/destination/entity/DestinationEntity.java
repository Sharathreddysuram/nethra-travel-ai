package ai.nethra.destination.entity;
import jakarta.persistence.*;
@Entity @Table(name="destinations")
public class DestinationEntity {
  @Id private String id;
  private String name;
  private String state;
  private java.math.BigDecimal rating;
  private java.math.BigDecimal hotelBasePerNight;
  private java.math.BigDecimal foodPerDay;
  public DestinationEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
