package ai.nethra.planner.llm;
import com.fasterxml.jackson.databind.ObjectMapper; import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.*;
@Configuration public class LlmConfig {
  @Bean public LlmClient llmClient(@Value("${openai.api-key:}") String apiKey,@Value("${openai.model:gpt-4o-mini}") String model,ObjectMapper mapper){ if(apiKey==null||apiKey.isBlank()) return SummaryTemplates::summarize; return new OpenAiLlmClient(apiKey,model,mapper); }
}
