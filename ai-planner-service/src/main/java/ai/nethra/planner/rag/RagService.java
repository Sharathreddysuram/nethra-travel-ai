package ai.nethra.planner.rag;
import ai.nethra.common.*; import org.springframework.stereotype.Service; import java.util.*;
@Service public class RagService {
  public List<String> highlights(DestinationProfile profile, PlanRequest request, int limit){
    InMemoryVectorStore store=new InMemoryVectorStore(); int i=0;
    for(String sentence: profile.guideText().split("(?<=[.!?])\s+")){ if(!sentence.isBlank()) store.add(new GuideSnippet(profile.id()+"-guide-"+(++i), profile.name(), sentence.trim())); }
    store.add(new GuideSnippet(profile.id()+"-tags",profile.name(),profile.name()+" is known for " + String.join(", ", profile.tags()) + ". Best months: " + String.join(", ", profile.bestMonths()) + "."));
    store.add(new GuideSnippet(profile.id()+"-fit",profile.name(),profile.name()+" suits trips of " + profile.idealDaysMin() + " to " + profile.idealDaysMax() + " days, rated " + profile.rating() + " by the Nethra mock guide corpus."));
    String query=String.join(" ", profile.tags()) + " best value " + request.daysValue() + " days " + request.transportMode();
    return store.search(query,limit).stream().map(GuideSnippet::text).toList();
  }
}
