package ai.nethra.planner;
import ai.nethra.common.*; import ai.nethra.planner.rag.RagService; import org.junit.jupiter.api.Test; import java.math.BigDecimal; import java.util.List; import static org.junit.jupiter.api.Assertions.*;
class RagServiceTest {
  @Test void retrievesGuideEvidenceForDestination(){ RagService rag=new RagService(); DestinationProfile goa=TravelCatalog.find("goa").orElseThrow(); List<String> highlights=rag.highlights(goa,new PlanRequest(new BigDecimal("30000"),"Mumbai",4,TransportMode.FLIGHT,1),3); assertFalse(highlights.isEmpty()); assertTrue(highlights.stream().anyMatch(h->h.toLowerCase().contains("goa")||h.toLowerCase().contains("beach")||h.toLowerCase().contains("water"))); }
}
