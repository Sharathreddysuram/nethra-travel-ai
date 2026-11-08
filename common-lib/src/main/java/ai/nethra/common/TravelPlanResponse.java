package ai.nethra.common;
import java.math.BigDecimal; import java.util.List; import java.util.Map;
public record TravelPlanResponse(boolean feasible, PlanRequest request, DestinationProfile destination, TransportOption transportOption, Hotel hotel, PartnerPackageQuote partnerPackage, List<ActivityItem> activities, ItineraryPlan itinerary, Map<BudgetCategory, BigDecimal> costBreakdown, BigDecimal totalCost, BigDecimal remainingBudget, String summary, List<String> guideHighlights, List<AgentStep> agentTrace, BigDecimal minimumRequiredBudget, String reason, List<ModeMinimum> alternativeTransportModes, List<DestinationMinimum> alternativeDestinations, List<String> suggestions) {
  public static TravelPlanResponse rejected(PlanRequest request, BigDecimal minimumRequiredBudget, String reason, List<ModeMinimum> modes, List<DestinationMinimum> destinations, List<String> suggestions, List<AgentStep> trace) {
    return new TravelPlanResponse(false, request, null, null, null, null, List.of(), null, Map.of(), Moneys.inr(0), request.budgetInr(), null, List.of(), trace, minimumRequiredBudget, reason, modes, destinations, suggestions);
  }
}
