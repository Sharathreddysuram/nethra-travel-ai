package ai.nethra.planner.client;
import ai.nethra.common.PartnerPackageQuote; import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*; import java.util.List;
@FeignClient(name="partner-service", url="${services.partner.url:http://localhost:8087}") public interface PartnerClient {
  @GetMapping("/api/partners/packages") List<PartnerPackageQuote> packages(@RequestParam("destinationId") String destinationId, @RequestParam("days") int days, @RequestParam("travelers") int travelers);
}
