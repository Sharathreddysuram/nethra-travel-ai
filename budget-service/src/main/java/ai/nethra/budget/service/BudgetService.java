package ai.nethra.budget.service;
import ai.nethra.common.*; import org.springframework.stereotype.Service;
import java.math.BigDecimal; import java.math.RoundingMode; import java.util.*;
@Service public class BudgetService {
  public BudgetAllocationResponse allocate(BudgetAllocationRequest req) {
    PlanRequest guard = new PlanRequest(req.budgetInr(), "Budget", req.days() == null ? 1 : req.days(), req.transportMode(), req.travelers());
    int days = guard.daysValue(); TransportMode mode = guard.transportMode();
    EnumMap<BudgetCategory, Double> f = new EnumMap<>(BudgetCategory.class);
    switch(mode) {
      case FLIGHT -> { f.put(BudgetCategory.TRANSPORT,.36); f.put(BudgetCategory.HOTEL,.27); f.put(BudgetCategory.FOOD,.16); f.put(BudgetCategory.ACTIVITIES,.13); }
      case TRAIN -> { f.put(BudgetCategory.TRANSPORT,.20); f.put(BudgetCategory.HOTEL,.33); f.put(BudgetCategory.FOOD,.20); f.put(BudgetCategory.ACTIVITIES,.19); }
      case BUS -> { f.put(BudgetCategory.TRANSPORT,.16); f.put(BudgetCategory.HOTEL,.35); f.put(BudgetCategory.FOOD,.22); f.put(BudgetCategory.ACTIVITIES,.19); }
      case CAR -> { f.put(BudgetCategory.TRANSPORT,.26); f.put(BudgetCategory.HOTEL,.31); f.put(BudgetCategory.FOOD,.19); f.put(BudgetCategory.ACTIVITIES,.16); }
    }
    if (days <= 2) { f.compute(BudgetCategory.TRANSPORT,(k,v)->v+.02); f.compute(BudgetCategory.HOTEL,(k,v)->v-.01); f.compute(BudgetCategory.ACTIVITIES,(k,v)->v-.01); }
    if (days >= 6) { f.compute(BudgetCategory.HOTEL,(k,v)->v+.03); f.compute(BudgetCategory.FOOD,(k,v)->v+.02); f.compute(BudgetCategory.TRANSPORT,(k,v)->v-.03); f.compute(BudgetCategory.ACTIVITIES,(k,v)->v-.02); }
    BigDecimal total = Moneys.round(req.budgetInr()); BigDecimal buffer = Moneys.percent(total, 8); BigDecimal spendable = total.subtract(buffer);
    double rawSum = f.values().stream().mapToDouble(Double::doubleValue).sum();
    EnumMap<BudgetCategory, BigDecimal> out = new EnumMap<>(BudgetCategory.class); BigDecimal assigned = BigDecimal.ZERO;
    for (var e : f.entrySet()) { BigDecimal amt = spendable.multiply(BigDecimal.valueOf(e.getValue()/rawSum)).setScale(2, RoundingMode.HALF_UP); out.put(e.getKey(), amt); assigned = assigned.add(amt); }
    out.put(BudgetCategory.HOTEL, out.get(BudgetCategory.HOTEL).add(spendable.subtract(assigned)));
    out.put(BudgetCategory.EMERGENCY_BUFFER, buffer);
    List<String> assumptions = List.of("Emergency buffer is always 8% of the total budget and is not spent by the planner.", "Starting split is mode-specific: " + mode + " trips reserve the share shown for transport before hotel, food and activities.", days >= 6 ? "Trips of 6+ days shift budget towards hotel and food because nightly costs dominate." : days <= 2 ? "Short trips shift budget towards transport because fixed journey costs dominate." : "Mid-length trips use the balanced mode split without a day adjustment.", "Hotel allocation assumes " + guard.rooms() + " room(s) for " + guard.travelersValue() + " traveller(s).");
    return new BudgetAllocationResponse(total, spendable, buffer, out, assumptions);
  }
  public BudgetPrecheckResponse precheck(BudgetAllocationRequest req) {
    BudgetAllocationResponse a = allocate(req); int days = req.days() == null ? 1 : req.days();
    BigDecimal perDay = a.spendableBudget().divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
    List<String> warnings = new ArrayList<>(); boolean plausible = true;
    if (a.totalBudget().compareTo(new BigDecimal("3000")) < 0) { plausible = false; warnings.add("Budgets below ₹3,000 rarely cover intercity transport plus one hotel night."); }
    if (perDay.compareTo(new BigDecimal("1200")) < 0) warnings.add("Spendable amount per day is below ₹1,200; expect very tight hotel and food choices.");
    if (req.transportMode() == TransportMode.FLIGHT && a.totalBudget().compareTo(new BigDecimal("18000")) < 0) warnings.add("Flight trips below ₹18,000 are frequently infeasible once hotel, food and the 8% buffer are included.");
    if (warnings.isEmpty()) warnings.add("Budget passes the generic precheck; destination-level feasibility still decides the final plan.");
    return new BudgetPrecheckResponse(plausible, a.totalBudget(), a.spendableBudget(), perDay, a, warnings);
  }
}
