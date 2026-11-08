package ai.nethra.planner.entity;
import jakarta.persistence.*;
@Entity @Table(name="travelplans")
public class TravelPlanEntity {
  @Id private String id;
  private String sourceCity;
  private java.math.BigDecimal budgetInr;
  private java.math.BigDecimal totalCost;
  private String status;
  private java.time.Instant createdAt;
  public TravelPlanEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
