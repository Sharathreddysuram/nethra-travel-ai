package ai.nethra.common;
public record NotificationRequest(String recipient, String channel, String subject, String message, String referenceId) {}
