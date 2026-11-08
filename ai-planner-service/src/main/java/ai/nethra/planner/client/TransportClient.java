package ai.nethra.planner.client;
import ai.nethra.common.*; import org.springframework.cloud.openfeign.FeignClient; import org.springframework.format.annotation.DateTimeFormat; import org.springframework.web.bind.annotation.*; import java.time.LocalDate; import java.util.List;
@FeignClient(name="transport-service", url="${services.transport.url:http://localhost:8084}") public interface TransportClient {
  @GetMapping("/api/transport/options") List<TransportOption> options(@RequestParam("sourceCity") String sourceCity, @RequestParam("destinationId") String destinationId, @RequestParam("mode") TransportMode mode, @RequestParam(value="date", required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("travelers") int travelers);
}
