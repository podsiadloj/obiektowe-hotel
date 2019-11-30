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
        if(!rooms.containsKey(name)){
            rooms.put(name, new RoomInfo(name, nOfBeds, comfort));
            saveRooms();
        }
    }

    @Override
    public void deleteRoom(String name) {
        if(!rooms.containsKey(name)){
            rooms.remove(name);
            saveClients();
        }
    }

    @Override
    public Map<String, RoomInfo> findFreeRooms(Period period, List<Integer> rooms) {
        List<String> results = new ArrayList<>();
        for (String roomName : this.rooms.keySet()) {
            RoomInfo room = this.rooms.get(roomName);
            for (int size : rooms){
                if(room.number == size){
                    if(!results.contains(roomName) && !isRoomTaken(period, roomName)){
                        results.add(roomName);
                    }
                }
            }
        }
        Map<String, RoomInfo> resultMap = new HashMap<>();
        for (String name : results) {
            resultMap.put(name, this.rooms.get(name));
        }
        return resultMap;
    }

    private boolean isRoomTaken(Period period, String roomName){
        for (ReservationInfo reservation : reservations.values()) {
            Period overlap = calcOverlap(period, reservation.getPeriod());
            if(overlap != null && overlap.getDays() > 0){
                return true;
            }
        }
        return false;
    }

    private Period calcOverlap(Period p1, Period p2){
        if(p1.getStart().getTime() < p2.getStart().getTime()){
            if(p1.getEnd().getTime() <= p2.getStart().getTime()){
                return null;
            } else {
                if(p1.getEnd().getTime() < p2.getEnd().getTime()){
                    return new Period( p2.getStart(), p1.getEnd() );
                } else {
                    return p2;
                }
            }
        } else {
            if(p1.getStart().getTime() > p2.getEnd().getTime()){
                return null;
            } else {
                if(p1.getEnd().getTime() > p2.getEnd().getTime()){
                    return new Period( p1.getStart(), p2.getEnd() );
                } else {
                    return p1;
                }
            }
        }
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
