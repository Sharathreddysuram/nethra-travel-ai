package ai.nethra.partner.entity;
import jakarta.persistence.*;
@Entity @Table(name="partnerpackages")
public class PartnerPackageEntity {
  @Id private String id;
  private String destinationId;
  private String name;
  private String partnerName;
  private java.math.BigDecimal pricePerNight;
  private java.math.BigDecimal tourBundlePrice;
  public PartnerPackageEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
