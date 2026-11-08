package ai.nethra.common;
import java.math.BigDecimal; import java.util.List;
public record PartnerPackage(String id, String destinationId, String name, String partnerName, String description, BigDecimal pricePerNight, BigDecimal tourBundlePrice, BigDecimal airportPickupValue, List<String> inclusions) {
  public BigDecimal quote(int days, int travelers) {
    int nights = Math.max(1, days - 1); int rooms = (travelers + 1) / 2;
    return Moneys.round(pricePerNight.multiply(BigDecimal.valueOf((long)nights * rooms)).add(tourBundlePrice.multiply(BigDecimal.valueOf(travelers))));
  }
}
