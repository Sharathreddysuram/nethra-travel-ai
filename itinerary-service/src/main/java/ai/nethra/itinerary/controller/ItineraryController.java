package ai.nethra.itinerary.controller;
import ai.nethra.common.*; import ai.nethra.itinerary.service.ItineraryService; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/itineraries") public class ItineraryController {
  private final ItineraryService service; public ItineraryController(ItineraryService service){this.service=service;}
  @PostMapping("/plan") public ItineraryPlan plan(@RequestBody ItineraryRequest req){ return service.plan(req); }
}
