package ai.nethra.destination;
import ai.nethra.common.*; import ai.nethra.destination.service.DestinationService; import org.junit.jupiter.api.Test; import java.math.BigDecimal; import static org.junit.jupiter.api.Assertions.*;
class DestinationServiceTest {
  private final DestinationService service=new DestinationService();
  @Test void catalogHasFourteenDestinations(){ assertEquals(14,service.all().size()); }
  @Test void hyderabadFlightFourDaysExceedsFifteenThousand(){ BigDecimal min=service.minimums("Hyderabad",4,1,TransportMode.FLIGHT).stream().map(DestinationMinimum::minimumRequiredBudget).min(BigDecimal::compareTo).orElseThrow(); System.out.println("Hyderabad 4d flight minimum="+min); assertTrue(min.compareTo(new BigDecimal("15000"))>0,"computed minimum was "+min); }
  @Test void hyderabadTrainThreeDaysHasFeasibleOption(){ BigDecimal min=service.minimums("Hyderabad",3,1,TransportMode.TRAIN).stream().map(DestinationMinimum::minimumRequiredBudget).min(BigDecimal::compareTo).orElseThrow(); assertTrue(min.compareTo(new BigDecimal("15000"))<=0,"computed minimum was "+min); }
  @Test void rankingExcludesSourceAndIsSorted(){ var ranks=service.rank("Hyderabad",TransportMode.TRAIN,new BigDecimal("15000"),3,1); assertFalse(ranks.isEmpty()); assertNotEquals("Hyderabad",ranks.get(0).destination().name()); for(int i=1;i<ranks.size();i++) assertTrue(ranks.get(i-1).score().compareTo(ranks.get(i).score())>=0); }
}
