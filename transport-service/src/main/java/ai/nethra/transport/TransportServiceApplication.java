package ai.nethra.transport;
import org.springframework.boot.SpringApplication; import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(proxyBeanMethods = false) public class TransportServiceApplication { public static void main(String[] args) { SpringApplication.run(TransportServiceApplication.class, args); } }
