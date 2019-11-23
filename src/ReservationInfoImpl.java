import java.util.List;

public class ReservationInfoImpl implements ReservationInfo {
    private Period period;
    private List<RoomInfo> rooms;

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
}
