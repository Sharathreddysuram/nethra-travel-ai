package ai.nethra.transport.controller;
import ai.nethra.common.*; import ai.nethra.transport.service.TransportService; import org.springframework.format.annotation.DateTimeFormat; import org.springframework.web.bind.annotation.*; import java.time.LocalDate; import java.util.List;
@RestController @RequestMapping("/api/transport") public class TransportController {
  private final TransportService service; public TransportController(TransportService service){this.service=service;}
  @GetMapping("/options") public List<TransportOption> options(@RequestParam String sourceCity,@RequestParam String destinationId,@RequestParam TransportMode mode,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,@RequestParam(defaultValue="1") int travelers){ return service.options(sourceCity,destinationId,mode,date,travelers); }
  @GetMapping("/cheapest") public TransportOption cheapest(@RequestParam String sourceCity,@RequestParam String destinationId,@RequestParam TransportMode mode,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,@RequestParam(defaultValue="1") int travelers){ return service.cheapest(sourceCity,destinationId,mode,date,travelers); }
  @GetMapping("/providers") public List<String> providers(){ return List.of("MockFlightProvider","MockTrainProvider","MockBusProvider","MockCarProvider"); }
}
