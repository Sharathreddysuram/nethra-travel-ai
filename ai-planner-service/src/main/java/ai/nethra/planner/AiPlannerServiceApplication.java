package ai.nethra.planner;
import org.springframework.boot.SpringApplication; import org.springframework.boot.autoconfigure.SpringBootApplication; import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication(proxyBeanMethods = false) @EnableFeignClients public class AiPlannerServiceApplication { public static void main(String[] args){ SpringApplication.run(AiPlannerServiceApplication.class,args); } }
