package ai.nethra.budget;
import ai.nethra.budget.service.BudgetService; import ai.nethra.common.*; import org.junit.jupiter.api.Test; import java.math.BigDecimal; import static org.junit.jupiter.api.Assertions.*;
class BudgetServiceTest {
  private final BudgetService service = new BudgetService();
  @Test void allocationsSumToBudgetAndHoldEightPercentBuffer(){ var r=service.allocate(new BudgetAllocationRequest(new BigDecimal("15000"),4,TransportMode.FLIGHT,1)); BigDecimal sum=r.allocations().values().stream().reduce(BigDecimal.ZERO,BigDecimal::add); assertEquals(0,new BigDecimal("15000.00").compareTo(sum)); assertEquals(0,new BigDecimal("1200.00").compareTo(r.emergencyBuffer())); assertEquals(5,r.allocations().size()); }
  @Test void flightReservesMoreForTransportThanTrain(){ var flight=service.allocate(new BudgetAllocationRequest(new BigDecimal("20000"),4,TransportMode.FLIGHT,1)); var train=service.allocate(new BudgetAllocationRequest(new BigDecimal("20000"),4,TransportMode.TRAIN,1)); assertTrue(flight.allocations().get(BudgetCategory.TRANSPORT).compareTo(train.allocations().get(BudgetCategory.TRANSPORT))>0); }
  @Test void precheckFlagsTinyBudgets(){ var r=service.precheck(new BudgetAllocationRequest(new BigDecimal("2000"),3,TransportMode.TRAIN,1)); assertFalse(r.plausible()); assertFalse(r.warnings().isEmpty()); }
}
