package ai.nethra.planner.tools;
import org.springframework.stereotype.Component; import java.util.*;
@Component public class ToolRegistry {
  public List<ToolDefinition> tools(){ return List.of(
    tool("plan_trip","Run the full Nethra agentic workflow and return a budget-validated travel plan or a structured infeasibility rejection.",props("budgetInr:number","sourceCity:string","days:integer","transportMode:string enum FLIGHT|TRAIN|BUS|CAR","travelers:integer")),
    tool("check_feasibility","Compute true minimum budgets by destination and transport mode before planning.",props("sourceCity:string","days:integer","travelers:integer")),
    tool("search_destinations","Rank curated destinations by value-for-money, rating, transport cost and days fit.",props("sourceCity:string","transportMode:string","budgetInr:number","days:integer")),
    tool("estimate_transport","Get deterministic mock transport options from the provider abstraction layer.",props("sourceCity:string","destinationId:string","mode:string","travelers:integer")),
    tool("find_hotels","Get value-ranked mock hotels for a destination.",props("destinationId:string")),
    tool("find_activities","List curated activities for a destination.",props("destinationId:string")),
    tool("find_partner_packages","Check local partner packages before open provider search.",props("destinationId:string","days:integer","travelers:integer")));
  }
  private ToolDefinition tool(String name,String desc,Map<String,Object> properties){ Map<String,Object> schema=new LinkedHashMap<>(); schema.put("type","object"); schema.put("properties",properties); return new ToolDefinition(name,desc,schema); }
  private Map<String,Object> props(String... kv){ Map<String,Object> m=new LinkedHashMap<>(); for(String s: kv){ String[] p=s.split(":",2); m.put(p[0],Map.of("type",p[1].split(" ")[0],"description",p[1])); } return m; }
}
