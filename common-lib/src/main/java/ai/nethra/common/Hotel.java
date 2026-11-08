package ai.nethra.common;
import java.math.BigDecimal;
public record Hotel(String id, String destinationId, String name, String area, int starTier, BigDecimal pricePerNight, BigDecimal rating, BigDecimal valueScore, boolean breakfastIncluded, boolean freeCancellation) {}
