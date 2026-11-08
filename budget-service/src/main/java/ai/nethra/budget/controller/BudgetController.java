package ai.nethra.budget.controller;
import ai.nethra.common.*; import ai.nethra.budget.service.BudgetService; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestController @RequestMapping("/api/budget") public class BudgetController {
  private final BudgetService service; public BudgetController(BudgetService service){this.service=service;}
  @PostMapping("/allocate") public BudgetAllocationResponse allocate(@RequestBody BudgetAllocationRequest req){ return service.allocate(req); }
  @PostMapping("/precheck") public BudgetPrecheckResponse precheck(@RequestBody BudgetAllocationRequest req){ return service.precheck(req); }
  @GetMapping("/health") public Map<String,String> health(){ return Map.of("service","budget-service","status","UP"); }
}
