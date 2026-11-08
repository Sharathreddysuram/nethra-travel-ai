package ai.nethra.common;
import java.math.BigDecimal;
public record PartnerPackageQuote(PartnerPackage partnerPackage, BigDecimal quoteTotal, BigDecimal estimatedSeparateCost, BigDecimal estimatedSavings, int days, int travelers) {}
