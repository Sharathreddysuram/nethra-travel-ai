package ai.nethra.planner.graph;
import ai.nethra.common.*; import java.math.BigDecimal; import java.util.*;
public class TravelGraphState {
  public final PlanRequest request; public final List<AgentStep> trace = new ArrayList<>();
  public BudgetAllocationResponse allocation; public List<DestinationMinimum> minimums = List.of(); public List<DestinationRank> ranked = List.of();
  public DestinationRank selectedRank; public DestinationProfile destination; public List<String> guideHighlights = List.of();
  public TransportOption transport; public PartnerPackageQuote partnerPackage; public Hotel hotel; public List<ActivityItem> activities = new ArrayList<>();
  public ItineraryPlan itinerary; public Map<BudgetCategory, BigDecimal> breakdown; public BigDecimal totalCost;
  public BigDecimal rejectionMinimum; public String rejectionReason; public List<ModeMinimum> rejectionModes = List.of(); public List<DestinationMinimum> rejectionDestinations = List.of(); public List<String> rejectionSuggestions = List.of();
  public TravelGraphState(PlanRequest request){ this.request=request; }
  public void addTrace(String agent,String decision,String detail){ trace.add(new AgentStep(agent,decision,detail)); }
  public BigDecimal buffer(){ return allocation != null ? allocation.emergencyBuffer() : request.emergencyBuffer(); }
  public BigDecimal foodCost(){ return destination == null ? BigDecimal.ZERO : TripCostEstimator.foodCost(destination, request.daysValue(), request.travelersValue()); }
  public BigDecimal lodgingCost(){ if(partnerPackage != null) return partnerPackage.quoteTotal(); if(hotel == null) return BigDecimal.ZERO; return hotel.pricePerNight().multiply(BigDecimal.valueOf((long)Math.max(0,request.nights())*request.rooms())); }
  public void resetCandidate(){ selectedRank=null; destination=null; guideHighlights=List.of(); transport=null; partnerPackage=null; hotel=null; activities=new ArrayList<>(); itinerary=null; breakdown=null; totalCost=null; }
}
