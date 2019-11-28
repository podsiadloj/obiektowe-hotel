import java.io.Reader;
import java.io.Writer;
import java.util.List;

public class HotelImpl implements Hotel {

    @Override
    public void addClient(String name) {

    }

    @Override
    public void deleteClient(String name) {

    }

    @Override
    public List<Client> clients() {
        return null;
    }

    @Override
    public void addRoom(String name, int nOfBeds, Comfort comfort) {

    }

    @Override
    public void deleteRoom(String name) {

    }

    @Override
    public List<ReservationInfo> findFreeRooms(Period period, List<Integer> rooms) {
        return null;
    }

    @Override
    public int makeReservation(Client client, ReservationInfo request) {
        return 0;
    }

    @Override
    public List<ReservationInfo> listReservations() {
        return null;
    }

    @Override
    public ReservationInfo getReservation(int id) {
        return null;
    }

    @Override
    public void deleteReservation(int id) {

    }

    public HotelImpl(){
        // musi załadować dane z CSV:
        // - pokoje
        // - klienci
        // - rezerwacje
        // te dane muszą być zapisywane w każdej funkcji modyfikującej stan
    }
}
