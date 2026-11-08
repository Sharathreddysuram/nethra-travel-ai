package ai.nethra.hotel.provider;
import ai.nethra.common.Hotel; import java.util.List;
public interface HotelProvider { List<Hotel> hotels(String destinationId); }
