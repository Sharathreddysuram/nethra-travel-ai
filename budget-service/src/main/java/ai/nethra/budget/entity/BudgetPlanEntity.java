package ai.nethra.budget.entity;
import jakarta.persistence.*;
@Entity @Table(name="budgetplans")
public class BudgetPlanEntity {
  @Id private String id;
  private java.math.BigDecimal totalBudget;
  private Integer days;
  private Integer travelers;
  private String transportMode;
  private java.time.Instant createdAt;
  public BudgetPlanEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
