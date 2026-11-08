package ai.nethra.transport.entity;
import jakarta.persistence.*;
@Entity @Table(name="transportquotes")
public class TransportQuoteEntity {
  @Id private String id;
  private String sourceCity;
  private String destinationName;
  private String mode;
  private java.math.BigDecimal price;
  private java.time.LocalDate travelDate;
  public TransportQuoteEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
