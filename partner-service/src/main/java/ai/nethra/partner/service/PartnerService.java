package ai.nethra.partner.service;
import ai.nethra.common.*; import org.springframework.stereotype.Service; import java.util.*;
@Service public class PartnerService {
  public List<PartnerPackageQuote> packages(String destinationId,int days,int travelers){ List<PartnerPackage> packs=destinationId==null?PartnerCatalog.all():PartnerCatalog.forDestination(destinationId); List<PartnerPackageQuote> out=new ArrayList<>(); for(PartnerPackage p: packs){ DestinationProfile profile=TravelCatalog.find(p.destinationId()).orElseThrow(); out.add(PartnerCatalog.quote(p,profile,days,travelers)); } out.sort(Comparator.comparing(PartnerPackageQuote::quoteTotal)); return out; }
}
