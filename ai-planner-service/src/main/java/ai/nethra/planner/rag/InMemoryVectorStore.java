package ai.nethra.planner.rag;
import java.util.*;
public class InMemoryVectorStore {
  private final Map<String, GuideSnippet> snippets=new LinkedHashMap<>(); private final Map<String,double[]> vectors=new HashMap<>();
  public void add(GuideSnippet snippet){ snippets.put(snippet.id(),snippet); vectors.put(snippet.id(),HashingEmbeddingModel.embed(snippet.text())); }
  public List<GuideSnippet> search(String query,int limit){ double[] q=HashingEmbeddingModel.embed(query); return snippets.values().stream().sorted(Comparator.comparingDouble((GuideSnippet s)->HashingEmbeddingModel.cosine(q,vectors.get(s.id()))).reversed()).limit(limit).toList(); }
}
