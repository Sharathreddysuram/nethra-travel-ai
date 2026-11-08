package ai.nethra.transport.provider;
import ai.nethra.common.TransportMode; import org.springframework.stereotype.Component; import java.util.List;
@Component public class MockCarProvider extends AbstractTransportProvider { public TransportMode mode(){ return TransportMode.CAR; } protected String providerName(){ return "DriveIndia Mock Rentals"; } protected List<String[]> windows(){ return List.of(new String[]{"07:00","1.00"},new String[]{"09:00","1.05"},new String[]{"11:00","1.10"}); } }
