package ai.nethra.hotel.controller;
import ai.nethra.common.Hotel; import ai.nethra.hotel.provider.HotelProvider; import org.springframework.web.bind.annotation.*; import java.math.BigDecimal; import java.util.List;
@RestController @RequestMapping("/api/hotels") public class HotelController {
  private final HotelProvider provider; public HotelController(HotelProvider provider){this.provider=provider;}
  @GetMapping public List<Hotel> hotels(@RequestParam String destinationId,@RequestParam(required=false) BigDecimal maxNightlyRate){ List<Hotel> all=provider.hotels(destinationId); return maxNightlyRate==null?all:all.stream().filter(h->h.pricePerNight().compareTo(maxNightlyRate)<=0).toList(); }
}
