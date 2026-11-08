package ai.nethra.user.service;
import ai.nethra.common.UserProfile; import jakarta.annotation.PostConstruct; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Component; import java.util.*; import java.util.concurrent.ConcurrentHashMap;
@Component public class UserStore {
  public record StoredUser(String id,String name,String email,String passwordHash,String role) { public UserProfile profile(){ return new UserProfile(id,name,email,role); } }
  private final Map<String,StoredUser> byEmail=new ConcurrentHashMap<>(); private final PasswordEncoder encoder;
  public UserStore(PasswordEncoder encoder){ this.encoder=encoder; }
  @PostConstruct void seed(){ registerInternal("Demo Traveller","demo@nethra.ai","password123","USER", true); }
  public StoredUser register(String name,String email,String password){ return registerInternal(name,email,password,"USER",false); }
  private StoredUser registerInternal(String name,String email,String password,String role,boolean seed){ String key=email.toLowerCase(Locale.ROOT); if(byEmail.containsKey(key)){ if(seed) return byEmail.get(key); throw new IllegalArgumentException("An account already exists for " + email); } StoredUser u=new StoredUser(UUID.randomUUID().toString(),name,email,encoder.encode(password),role); byEmail.put(key,u); return u; }
  public Optional<StoredUser> find(String email){ return email==null?Optional.empty():Optional.ofNullable(byEmail.get(email.toLowerCase(Locale.ROOT))); }
  public boolean passwordMatches(StoredUser u,String raw){ return encoder.matches(raw,u.passwordHash()); }
}
