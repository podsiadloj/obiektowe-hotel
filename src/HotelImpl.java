import java.util.*;

public class HotelImpl implements Hotel {

    private Map<String, Client> clientsMap;
    private Map<String, RoomInfo> rooms;
    private Map<Integer, ReservationInfo> reservations;

    private void saveClients(){
        //TODO: save clients to csv
    };
    private void saveRooms(){
        //TODO: save rooms to csv
    };
    private void saveReservations(){
        //TODO: save reservations to csv
    };

    @Override
    public void addClient(String name) {
        if(!clientsMap.containsKey(name)){
            clientsMap.put(name, new Client());
            saveClients();
        }
    }

    @Override
    public void deleteClient(String name) {
        if(!clientsMap.containsKey(name)){
            clientsMap.remove(name);
            saveClients();
        }
    }

    @Override
    public List<String> clients() {
        return new ArrayList<>(this.clientsMap.keySet());
    }

    @Override
    public Client getClient(String name) {
        return clientsMap.get(name);
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
        clientsMap = new HashMap<>(); //TODO: load from csv
        rooms = new HashMap<>(); //TODO: load from csv
        reservations = new HashMap<>(); //TODO: load from csv
        // musi załadować dane z CSV:
        // - pokoje
        // - klienci
        // - rezerwacje
        // te dane muszą być zapisywane w każdej funkcji modyfikującej stan
    }
}
