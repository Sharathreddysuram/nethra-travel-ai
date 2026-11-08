package ai.nethra.common;
import java.math.BigDecimal; import java.util.List;
public record DestinationProfile(String id, String name, String state, String region, String hubCity, double latitude, double longitude, BigDecimal rating, BigDecimal hotelBasePerNight, BigDecimal foodPerDay, BigDecimal activityBasePerDay, List<String> bestMonths, List<String> tags, int idealDaysMin, int idealDaysMax, String guideText) {
  public BigDecimal costIndex() { return hotelBasePerNight.add(foodPerDay).add(activityBasePerDay).divide(BigDecimal.valueOf(1000), 3, java.math.RoundingMode.HALF_UP); }
}
