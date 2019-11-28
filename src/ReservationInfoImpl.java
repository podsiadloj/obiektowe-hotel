import java.util.List;

public class ReservationInfoImpl implements ReservationInfo {
    private Period period;
    private List<RoomInfo> rooms;
    private int price = 0;

    public ReservationInfoImpl(Period period, List<RoomInfo> rooms) {
        this.period = period;
        this.rooms = rooms;
    }

    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public List<RoomInfo> getRoomsInfo() {
        return rooms;
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
