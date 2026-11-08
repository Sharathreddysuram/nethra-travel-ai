package ai.nethra.common;
import java.math.BigDecimal;
public record DestinationRank(DestinationProfile destination, BigDecimal score, BigDecimal minimumRequiredBudget, BigDecimal estimatedTransportCost, String rationale) {}
