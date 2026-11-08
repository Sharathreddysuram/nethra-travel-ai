package ai.nethra.hotel.entity;
import jakarta.persistence.*;
@Entity @Table(name="hotels")
public class HotelEntity {
  @Id private String id;
  private String destinationId;
  private String name;
  private Integer starTier;
  private java.math.BigDecimal pricePerNight;
  private java.math.BigDecimal rating;
  public HotelEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
