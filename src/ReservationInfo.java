import java.util.*;

public interface ReservationInfo {
    Period getPeriod();
    List<RoomInfo> getRoomsInfo();
    Integer getPrice();
    void setPrice(int price);
}
