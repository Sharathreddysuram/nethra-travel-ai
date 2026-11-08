package ai.nethra.partner.controller;
import ai.nethra.common.PartnerPackageQuote; import ai.nethra.partner.service.PartnerService; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/partners") public class PartnerController {
  private final PartnerService service; public PartnerController(PartnerService service){this.service=service;}
  @GetMapping("/packages") public List<PartnerPackageQuote> packages(@RequestParam(required=false) String destinationId,@RequestParam(defaultValue="4") int days,@RequestParam(defaultValue="1") int travelers){ return service.packages(destinationId,days,travelers); }
}
