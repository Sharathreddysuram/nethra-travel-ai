package ai.nethra.planner.llm;
import ai.nethra.planner.graph.TravelGraphState;
public interface LlmClient { String summarize(TravelGraphState state); }
