package ai.nethra.planner.client;
import ai.nethra.common.*; import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*;
@FeignClient(name="itinerary-service", url="${services.itinerary.url:http://localhost:8088}") public interface ItineraryClient {
  @PostMapping("/api/itineraries/plan") ItineraryPlan plan(@RequestBody ItineraryRequest request);
}
