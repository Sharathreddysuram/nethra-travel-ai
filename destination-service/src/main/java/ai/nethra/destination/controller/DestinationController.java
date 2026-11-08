package ai.nethra.destination.controller;
import ai.nethra.common.*; import ai.nethra.destination.service.DestinationService; import org.springframework.web.bind.annotation.*; import java.math.BigDecimal; import java.util.List;
@RestController @RequestMapping("/api/destinations") public class DestinationController {
  private final DestinationService service; public DestinationController(DestinationService service){this.service=service;}
  @GetMapping public List<DestinationProfile> all(){ return service.all(); }
  @GetMapping("/{id}") public DestinationProfile get(@PathVariable String id){ return service.get(id); }
  @GetMapping("/{id}/guide") public java.util.Map<String,String> guide(@PathVariable String id){ var d=service.get(id); return java.util.Map.of("destination",d.name(),"guide",d.guideText()); }
  @GetMapping("/rank") public List<DestinationRank> rank(@RequestParam String sourceCity,@RequestParam TransportMode transportMode,@RequestParam BigDecimal budgetInr,@RequestParam int days,@RequestParam(defaultValue="1") int travelers){ return service.rank(sourceCity,transportMode,budgetInr,days,travelers); }
  @GetMapping("/minimums") public List<DestinationMinimum> minimums(@RequestParam String sourceCity,@RequestParam int days,@RequestParam(defaultValue="1") int travelers,@RequestParam(required=false) TransportMode transportMode){ return service.minimums(sourceCity,days,travelers,transportMode); }
}
