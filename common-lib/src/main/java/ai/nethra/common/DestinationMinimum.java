package ai.nethra.common;
import java.math.BigDecimal;
public record DestinationMinimum(String destinationId, String destinationName, TransportMode transportMode, BigDecimal transportCost, BigDecimal lodgingAndActivityCost, BigDecimal foodCost, BigDecimal subtotalBeforeBuffer, BigDecimal minimumRequiredBudget, boolean partnerPackageAvailable) {}
