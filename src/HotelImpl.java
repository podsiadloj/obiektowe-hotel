import java.io.Reader;
import java.io.Writer;
import java.util.List;

public class HotelImpl implements Hotel {
    @Override
    public void loadRooms(Reader reader) {

    }

    @Override
    public void saveRooms(Writer writer) {

    }

    @Override
    public void addRoom(String name, int nOfBeds) {

    }

    @Override
    public void deleteRoom(String name) {

    }

    @Override
    public List<ReservationInfo> findFreeRooms(Period period, List<Integer> rooms) {
        return null;
    }

    @Override
    public boolean makeReservation(Client client, ReservationInfo request) {
        return false;
    }

    public HotelImpl(){
        // musi załadować dane z CSV:
        // - pokoje
        // - klienci
        // - rezerwacje
        // te dane muszą być zapisywane w każdej funkcji modyfikującej stan
    }
}
