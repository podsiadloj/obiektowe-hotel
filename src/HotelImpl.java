import javax.management.openmbean.InvalidKeyException;
import javax.management.openmbean.KeyAlreadyExistsException;
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
    public void addRoom(String name, int nOfBeds, Comfort comfort) throws KeyAlreadyExistsException {
        if(!rooms.containsKey(name)){
            rooms.put(name, new RoomInfo(name, nOfBeds, comfort));
            saveRooms();
        } else throw new KeyAlreadyExistsException();
    }

    public RoomInfo getRoom(String name) throws InvalidKeyException {
        if(rooms.containsKey(name)){
            return rooms.get(name);
        } else throw new InvalidKeyException();
    }

    @Override
    public void deleteRoom(String name) throws InvalidKeyException {
        if(!rooms.containsKey(name)){
            rooms.remove(name);
            saveRooms();
        } else throw new InvalidKeyException();
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
                for (RoomInfo reservedRoom: reservation.getRoomsInfo()){
                    if(reservedRoom.name.equals(roomName)) {
                        return true;
                    }
                }
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
    public int makeReservation(Client client, ReservationInfo request) throws InvalidRequestException {
        for (RoomInfo room : request.getRoomsInfo()) {
            if(isRoomTaken(request.getPeriod(), room.name)){
                throw new InvalidRequestException();
            }
        }
        request.setPrice(calcReservationCost(request, client));
        int id = newReservationId();
        reservations.put(id, request);
        client.reservationIds.add(id);
        if(client.reservationIds.size() >= 5){
            client.discount = true;
        }
        saveReservations();
        saveClients();
        return request.getPrice();
    }

    private int newReservationId(){
        int i = 0;
        while(true){
            if(!reservations.containsKey(i)){
                return i;
            } else {
                i++;
            }
        }
    }

    private int calcReservationCost(ReservationInfo reservation, Client client) {
        int base = reservation.getRoomsInfo().stream().mapToInt(RoomInfo::getDailyCost).sum();
        int forPeriod = base * reservation.getPeriod().getDays();
        if(client.discount){
            return (int) Math.ceil(forPeriod * 0.8);
        } else {
            return forPeriod;
        }
    }

    @Override
    public List<Map.Entry<Integer, ReservationInfo>> listReservations() {
        return new ArrayList<>(reservations.entrySet());
    }

    @Override
    public ReservationInfo getReservation(int id) {
        return reservations.get(id);
    }

    @Override
    public void deleteReservation(int id) {
        if(reservations.containsKey(id)){
            reservations.remove(id);
            for (Client client: clientsMap.values()) {
                client.reservationIds.removeIf(i -> i == id);
            }
            saveReservations();
            saveClients();
        }
    }

    private HotelImpl(){
        clientsMap = new HashMap<>(); //TODO: load from csv
        rooms = new HashMap<>(); //TODO: load from csv
        reservations = new HashMap<>(); //TODO: load from csv
        // musi załadować dane z CSV:
        // - pokoje
        // - klienci
        // - rezerwacje
        // te dane muszą być zapisywane w każdej funkcji modyfikującej stan
    }

    public static Hotel instance = new HotelImpl();

    public static Hotel getInstance(){
        return instance;
    }
}
