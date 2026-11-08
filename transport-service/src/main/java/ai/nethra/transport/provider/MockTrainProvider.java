package ai.nethra.transport.provider;
import ai.nethra.common.TransportMode; import org.springframework.stereotype.Component; import java.util.List;
@Component public class MockTrainProvider extends AbstractTransportProvider { public TransportMode mode(){ return TransportMode.TRAIN; } protected String providerName(){ return "Bharat Rail Mock"; } protected List<String[]> windows(){ return List.of(new String[]{"05:50","1.00"},new String[]{"15:30","1.06"},new String[]{"22:15","1.12"}); } }
