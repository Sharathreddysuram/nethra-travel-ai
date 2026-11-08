package ai.nethra.activity.controller;
import ai.nethra.common.ActivityItem; import ai.nethra.activity.provider.ActivityProvider; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/activities") public class ActivityController {
  private final ActivityProvider provider; public ActivityController(ActivityProvider provider){this.provider=provider;}
  @GetMapping public List<ActivityItem> activities(@RequestParam String destinationId,@RequestParam(required=false) String category){ List<ActivityItem> all=provider.activities(destinationId); return category==null?all:all.stream().filter(a->a.category().equalsIgnoreCase(category)).toList(); }
  @GetMapping("/categories") public Set<String> categories(@RequestParam String destinationId){ Set<String> out=new TreeSet<>(); provider.activities(destinationId).forEach(a->out.add(a.category())); return out; }
}
