package ai.nethra.user.service;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import javax.crypto.SecretKey; import java.nio.charset.StandardCharsets; import java.time.Instant; import java.util.Date;
@Service public class JwtService {
  private final SecretKey key; private final long ttlSeconds;
  public JwtService(@Value("${nethra.jwt.secret:dev-secret-key-for-nethra-travel-ai-change-me-2026}") String secret, @Value("${nethra.jwt.ttl-seconds:86400}") long ttlSeconds){ this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.ttlSeconds=ttlSeconds; }
  public String issue(String userId, String email, String role){ Instant now=Instant.now(); return Jwts.builder().subject(email).claim("userId",userId).claim("role",role).issuer("nethra-user-service").issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(ttlSeconds))).signWith(key).compact(); }
  public long ttlSeconds(){ return ttlSeconds; }
  public Claims parse(String token){ return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }
}
