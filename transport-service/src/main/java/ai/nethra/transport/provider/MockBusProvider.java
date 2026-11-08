package ai.nethra.transport.provider;
import ai.nethra.common.TransportMode; import org.springframework.stereotype.Component; import java.util.List;
@Component public class MockBusProvider extends AbstractTransportProvider { public TransportMode mode(){ return TransportMode.BUS; } protected String providerName(){ return "HighwayLine Mock Coaches"; } protected List<String[]> windows(){ return List.of(new String[]{"06:00","1.00"},new String[]{"12:00","1.05"},new String[]{"21:30","1.10"}); } }
