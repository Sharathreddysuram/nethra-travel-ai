package ai.nethra.transport.provider;
import ai.nethra.common.TransportMode; import org.springframework.stereotype.Component; import java.util.List;
@Component public class MockFlightProvider extends AbstractTransportProvider { public TransportMode mode(){ return TransportMode.FLIGHT; } protected String providerName(){ return "IndiSky Mock Air"; } protected List<String[]> windows(){ return List.of(new String[]{"06:10","1.08"},new String[]{"13:25","1.00"},new String[]{"20:40","1.16"}); } }
