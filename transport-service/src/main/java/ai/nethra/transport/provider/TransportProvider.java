package ai.nethra.transport.provider;
import ai.nethra.common.*; import java.time.LocalDate; import java.util.List;
public interface TransportProvider { TransportMode mode(); List<TransportOption> options(String sourceCity, String destinationId, LocalDate travelDate, int travelers); }
