import java.util.List;
import java.util.stream.Collectors;

public class ReservationInfoImpl implements ReservationInfo {
    private Period period;
    private List<String> roomNames;
    private int price = 0;

    public ReservationInfoImpl(Period period, List<String> rooms) {
        this.period = period;
        this.roomNames = rooms;
    }

    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public List<RoomInfo> getRoomsInfo() {
        Hotel hotel = HotelImpl.getInstance();
        return roomNames.stream().map(hotel::getRoom).collect(Collectors.toList());
    }

    @Override
    public Integer getPrice() {
        return price;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }
}
