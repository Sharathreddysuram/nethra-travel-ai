package ai.nethra.planner.client;
import ai.nethra.common.Hotel; import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*; import java.math.BigDecimal; import java.util.List;
@FeignClient(name="hotel-service", url="${services.hotel.url:http://localhost:8085}") public interface HotelClient {
  @GetMapping("/api/hotels") List<Hotel> hotels(@RequestParam("destinationId") String destinationId, @RequestParam(value="maxNightlyRate", required=false) BigDecimal maxNightlyRate);
}
