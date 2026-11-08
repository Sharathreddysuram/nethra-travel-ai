package ai.nethra.planner.client;
import ai.nethra.common.*; import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*; import java.math.BigDecimal; import java.util.List;
@FeignClient(name="destination-service", url="${services.destination.url:http://localhost:8083}") public interface DestinationClient {
  @GetMapping("/api/destinations/rank") List<DestinationRank> rank(@RequestParam("sourceCity") String sourceCity, @RequestParam("transportMode") TransportMode transportMode, @RequestParam("budgetInr") BigDecimal budgetInr, @RequestParam("days") int days, @RequestParam("travelers") int travelers);
  @GetMapping("/api/destinations/minimums") List<DestinationMinimum> minimums(@RequestParam("sourceCity") String sourceCity, @RequestParam("days") int days, @RequestParam("travelers") int travelers, @RequestParam(value="transportMode", required=false) TransportMode transportMode);
}
