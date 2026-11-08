package ai.nethra.common;
import java.math.BigDecimal; import java.util.*;
public final class PartnerCatalog {
  private PartnerCatalog() {}
  private static final List<PartnerPackage> PACKAGES = List.of(
    new PartnerPackage("goa-partner-classic","goa","Goa Partner Package","Konkan Local Partners","Hotel, airport pickup and local tours bundled by a Goa-based partner team.",new BigDecimal("2600"),new BigDecimal("1800"),new BigDecimal("800"),List.of("Partner hotel stay","Airport pickup","North Goa local tour","Sunset cruise credit")),
    new PartnerPackage("manali-partner-mountain","manali","Manali Partner Package","Himalaya Host Collective","Mountain hotel, transfers and guided valley experiences in one bundle.",new BigDecimal("2400"),new BigDecimal("1500"),new BigDecimal("700"),List.of("Partner hotel stay","Station pickup","Solang Valley tour","Old Manali walk")),
    new PartnerPackage("jaipur-partner-heritage","jaipur","Jaipur Partner Package","Pink City Hosts","Heritage hotel, fort guiding and bazaar experiences bundled locally.",new BigDecimal("2200"),new BigDecimal("1200"),new BigDecimal("600"),List.of("Partner hotel stay","Station pickup","Amber Fort guide","Food walk credit")),
    new PartnerPackage("kochi-partner-backwater","kochi","Kerala Partner Package","Malabar Travel Circle","Kochi hotel, harbour pickup and backwater experiences in one price.",new BigDecimal("2100"),new BigDecimal("1200"),new BigDecimal("600"),List.of("Partner hotel stay","Airport pickup","Fort Kochi walk","Backwater cruise credit")),
    new PartnerPackage("udaipur-partner-lakes","udaipur","Udaipur Partner Package","Lake City Partners","Lake-view partner hotel, palace guiding and boat experience bundle.",new BigDecimal("2400"),new BigDecimal("1300"),new BigDecimal("600"),List.of("Partner hotel stay","Station pickup","City Palace guide","Lake Pichola boat credit")),
    new PartnerPackage("darjeeling-partner-tea","darjeeling","Darjeeling Partner Package","Himalayan Tea Trails","Hill hotel, station transfer and tea-garden experiences bundled.",new BigDecimal("2200"),new BigDecimal("1200"),new BigDecimal("700"),List.of("Partner hotel stay","Station pickup","Tiger Hill trip","Tea garden tour")),
    new PartnerPackage("mysuru-partner-heritage","mysuru","Mysuru Partner Package","Cauvery Heritage Hosts","Value heritage bundle for short Mysuru trips.",new BigDecimal("1900"),new BigDecimal("1000"),new BigDecimal("500"),List.of("Partner hotel stay","Station pickup","Palace guide","Market walk"))
  );
  public static List<PartnerPackage> all(){ return PACKAGES; }
  public static List<PartnerPackage> forDestination(String destinationId){ return PACKAGES.stream().filter(p->p.destinationId().equalsIgnoreCase(destinationId)).toList(); }
  public static PartnerPackageQuote quote(PartnerPackage pack, DestinationProfile profile, int days, int travelers){
    BigDecimal quote=pack.quote(days,travelers); int nights=Math.max(1,days-1); int rooms=(travelers+1)/2;
    BigDecimal hotel=profile.hotelBasePerNight().multiply(BigDecimal.valueOf((long)nights*rooms));
    List<ActivityItem> acts=ActivityCatalog.forDestination(profile.id()).stream().sorted(Comparator.comparing(ActivityItem::price).reversed()).limit(3).toList();
    BigDecimal tours=acts.stream().map(ActivityItem::price).reduce(BigDecimal.ZERO,BigDecimal::add).multiply(BigDecimal.valueOf(travelers));
    BigDecimal separate=hotel.add(tours).add(pack.airportPickupValue());
    BigDecimal savings=separate.subtract(quote); if(savings.signum()<0) savings=BigDecimal.ZERO;
    return new PartnerPackageQuote(pack,Moneys.round(quote),Moneys.round(separate),Moneys.round(savings),days,travelers);
  }
}
