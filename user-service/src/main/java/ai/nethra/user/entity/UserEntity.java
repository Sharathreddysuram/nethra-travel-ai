package ai.nethra.user.entity;
import jakarta.persistence.*;
@Entity @Table(name="users")
public class UserEntity {
  @Id private String id;
  private String name;
  private String email;
  private String passwordHash;
  private String role;
  private java.time.Instant createdAt;
  public UserEntity() {}
  public String getId() { return id; } public void setId(String id) { this.id = id; }
}
