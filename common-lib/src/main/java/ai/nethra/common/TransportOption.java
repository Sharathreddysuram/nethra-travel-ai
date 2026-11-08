package ai.nethra.common;
import java.math.BigDecimal; import java.time.LocalDate;
public record TransportOption(String id, TransportMode mode, String providerName, String sourceCity, String destinationName, LocalDate travelDate, String departureWindow, BigDecimal price, BigDecimal durationHours, boolean refundable, BigDecimal valueScore) {}
