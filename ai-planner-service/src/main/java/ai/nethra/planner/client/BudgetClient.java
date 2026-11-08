package ai.nethra.planner.client;
import ai.nethra.common.*; import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*;
@FeignClient(name="budget-service", url="${services.budget.url:http://localhost:8082}") public interface BudgetClient {
  @PostMapping("/api/budget/allocate") BudgetAllocationResponse allocate(@RequestBody BudgetAllocationRequest request);
}
