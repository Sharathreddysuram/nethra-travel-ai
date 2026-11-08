package ai.nethra.planner.agent;
import ai.nethra.common.*; import ai.nethra.planner.graph.TravelGraphState; import org.springframework.stereotype.Component; import java.math.BigDecimal; import java.math.RoundingMode; import java.util.*;
@Component public class BudgetValidationAgent {
  public boolean validate(TravelGraphState s){
    EnumMap<BudgetCategory,BigDecimal> b=new EnumMap<>(BudgetCategory.class);
    BigDecimal transport=s.transport.price(); BigDecimal hotel; BigDecimal activities;
    if(s.partnerPackage!=null){ BigDecimal q=s.partnerPackage.quoteTotal(); BigDecimal t=Moneys.rupees(q.multiply(new BigDecimal("0.10"))); BigDecimal a=Moneys.rupees(q.multiply(new BigDecimal("0.20"))); BigDecimal h=q.subtract(t).subtract(a); transport=transport.add(t); hotel=h; activities=a; }
    else { hotel=s.lodgingCost(); activities=BigDecimal.ZERO; }
    BigDecimal extras=s.activities.stream().map(a->a.price().multiply(BigDecimal.valueOf(s.request.travelersValue()))).reduce(BigDecimal.ZERO,BigDecimal::add); activities=activities.add(extras);
    b.put(BudgetCategory.TRANSPORT,Moneys.round(transport)); b.put(BudgetCategory.HOTEL,Moneys.round(hotel)); b.put(BudgetCategory.FOOD,Moneys.round(s.foodCost())); b.put(BudgetCategory.ACTIVITIES,Moneys.round(activities)); b.put(BudgetCategory.EMERGENCY_BUFFER,Moneys.round(s.buffer()));
    s.breakdown=b; s.totalCost=b.values().stream().reduce(BigDecimal.ZERO,BigDecimal::add);
    if(s.totalCost.compareTo(s.request.budgetInr())>0){ s.addTrace("BudgetValidationAgent","OVER_BUDGET","Candidate " + s.destination.name() + " totals " + Moneys.format(s.totalCost) + ", above the " + Moneys.format(s.request.budgetInr()) + " budget; trying the next ranked destination."); return false; }
    s.addTrace("BudgetValidationAgent","WITHIN_BUDGET","Final total " + Moneys.format(s.totalCost) + " is within the " + Moneys.format(s.request.budgetInr()) + " budget, leaving " + Moneys.format(s.request.budgetInr().subtract(s.totalCost)) + " unspent (in addition to the protected emergency buffer line).");
    return true;
  }
  public TravelPlanResponse buildRejection(TravelGraphState s){ s.addTrace("BudgetValidationAgent","REJECTED","No itinerary generated. Minimum required: " + Moneys.format(s.rejectionMinimum) + ". Alternatives and suggestions are returned instead."); return TravelPlanResponse.rejected(s.request,s.rejectionMinimum,s.rejectionReason,s.rejectionModes,s.rejectionDestinations,s.rejectionSuggestions,List.copyOf(s.trace)); }
}
