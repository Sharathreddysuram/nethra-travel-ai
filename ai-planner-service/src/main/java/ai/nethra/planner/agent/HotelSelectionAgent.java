package ai.nethra.planner.agent;
import ai.nethra.common.*; import ai.nethra.planner.client.*; import ai.nethra.planner.graph.TravelGraphState; import org.springframework.stereotype.Component; import java.math.BigDecimal; import java.util.*;
@Component public class HotelSelectionAgent {
  private final PartnerClient partners; private final HotelClient hotels;
  public HotelSelectionAgent(PartnerClient partners,HotelClient hotels){this.partners=partners;this.hotels=hotels;}
  public void execute(TravelGraphState s){
    BigDecimal mandatory=s.transport.price().add(s.foodCost()).add(s.buffer());
    List<PartnerPackageQuote> quotes=List.of(); try { quotes=partners.packages(s.destination.id(),s.request.daysValue(),s.request.travelersValue()); } catch(Exception ignored) {}
    for(PartnerPackageQuote q: quotes){ if(q.quoteTotal().add(mandatory).compareTo(s.request.budgetInr())<=0){ s.partnerPackage=q; s.addTrace("HotelSelectionAgent","PARTNER_PACKAGE_SELECTED","Partner packages are checked before open hotel search. " + q.partnerPackage().name() + " fits at " + Moneys.format(q.quoteTotal()) + " (estimated saving " + Moneys.format(q.estimatedSavings()) + " vs separate hotel + tours), so no separate hotel was booked."); return; } }
    if(s.request.nights()==0){ s.addTrace("HotelSelectionAgent","NO_OVERNIGHT_STAY","One-day trip: no hotel night is required."); return; }
    List<Hotel> options=hotels.hotels(s.destination.id(),null); if(options.isEmpty()) throw new IllegalStateException("No hotels returned");
    s.hotel=options.stream().min(Comparator.comparing(Hotel::pricePerNight)).orElseThrow();
    s.addTrace("HotelSelectionAgent","HOTEL_SELECTED","No fitting partner package, so the lowest-priced mock hotel was selected: " + s.hotel.name() + " at " + Moneys.format(s.hotel.pricePerNight()) + "/night (value score " + s.hotel.valueScore() + ").");
  }
}
