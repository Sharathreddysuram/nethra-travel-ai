package ai.nethra.user.controller;
import ai.nethra.common.*; import ai.nethra.user.service.*; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/users") public class UserController {
  private final UserStore store; private final JwtService jwt;
  public UserController(UserStore store,JwtService jwt){this.store=store;this.jwt=jwt;}
  @PostMapping("/register") public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest req){ try { var u=store.register(req.name()==null?"Nethra Traveller":req.name(),req.email(),req.password()); return ResponseEntity.status(HttpStatus.CREATED).body(auth(u)); } catch(IllegalArgumentException e){ throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage()); } }
  @PostMapping("/login") public AuthResponse login(@RequestBody AuthRequest req){ var u=store.find(req.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password")); if(!store.passwordMatches(u,req.password())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password"); return auth(u); }
  @GetMapping("/me") public UserProfile me(@RequestHeader("Authorization") String authorization){ return profileFromToken(authorization); }
  @PostMapping("/validate") public UserProfile validate(@RequestHeader("Authorization") String authorization){ return profileFromToken(authorization); }
  private AuthResponse auth(UserStore.StoredUser u){ return new AuthResponse(jwt.issue(u.id(),u.email(),u.role()),"Bearer",jwt.ttlSeconds(),u.profile()); }
  private UserProfile profileFromToken(String authorization){ if(authorization==null||!authorization.startsWith("Bearer ")) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Missing bearer token"); try { var claims=jwt.parse(authorization.substring(7)); String email=claims.getSubject(); return store.find(email).map(UserStore.StoredUser::profile).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unknown user")); } catch(ResponseStatusException e){ throw e; } catch(Exception e){ throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid or expired token"); } }
}
