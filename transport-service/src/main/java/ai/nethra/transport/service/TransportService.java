package ai.nethra.transport.service;
import ai.nethra.common.*; import ai.nethra.transport.provider.TransportProvider; import org.springframework.stereotype.Service; import java.time.LocalDate; import java.util.*;
@Service public class TransportService {
  private final Map<TransportMode,TransportProvider> providers=new EnumMap<>(TransportMode.class);
  public TransportService(List<TransportProvider> list){ list.forEach(p->providers.put(p.mode(),p)); }
  public List<TransportOption> options(String source,String destination,TransportMode mode,LocalDate date,int travelers){ TransportProvider p=providers.get(mode); if(p==null) throw new IllegalArgumentException("No mock provider for " + mode); return p.options(source,destination,date,travelers); }
  public TransportOption cheapest(String source,String destination,TransportMode mode,LocalDate date,int travelers){ return options(source,destination,mode,date,travelers).stream().min(Comparator.comparing(TransportOption::price)).orElseThrow(); }
}
