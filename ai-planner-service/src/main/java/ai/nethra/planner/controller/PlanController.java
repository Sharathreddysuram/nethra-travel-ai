package ai.nethra.planner.controller;
import ai.nethra.common.*; import ai.nethra.planner.client.NotificationClient; import ai.nethra.planner.graph.TravelGraph; import ai.nethra.planner.tools.*; import org.slf4j.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/ai") public class PlanController {
  private static final Logger log=LoggerFactory.getLogger(PlanController.class);
  private final TravelGraph graph; private final NotificationClient notifications; private final ToolRegistry tools;
  public PlanController(TravelGraph graph,NotificationClient notifications,ToolRegistry tools){this.graph=graph;this.notifications=notifications;this.tools=tools;}
  @PostMapping("/plan") public TravelPlanResponse plan(@RequestBody PlanRequest request){ TravelPlanResponse response=graph.run(request); if(response.feasible()){ try { notifications.send(new NotificationRequest("demo@nethra.ai","EMAIL","Your Nethra travel plan is ready","Your " + response.destination().name() + " plan totals " + Moneys.format(response.totalCost()) + ". " + response.summary(), response.destination().id())); } catch(Exception e){ log.warn("Notification service unavailable; plan still returned: {}", e.getMessage()); } } return response; }
  @GetMapping("/tools") public List<ToolDefinition> tools(){ return tools.tools(); }
  @GetMapping("/health") public Map<String,String> health(){ return Map.of("service","ai-planner-service","status","UP","workflow","BudgetAnalysis>Feasibility>Destination>Transport>Hotel>Activity>Itinerary>BudgetValidation"); }
}
