package ai.nethra.common;
public record AuthResponse(String token, String tokenType, long expiresInSeconds, UserProfile user) {}
