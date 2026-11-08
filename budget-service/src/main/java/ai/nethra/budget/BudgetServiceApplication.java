package ai.nethra.budget;
import org.springframework.boot.SpringApplication; import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(proxyBeanMethods = false) public class BudgetServiceApplication { public static void main(String[] args) { SpringApplication.run(BudgetServiceApplication.class, args); } }
