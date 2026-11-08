package ai.nethra.common;
import java.math.BigDecimal;
public record ModeMinimum(TransportMode transportMode, String destinationName, BigDecimal minimumRequiredBudget) {}
