import javax.management.openmbean.InvalidKeyException;
import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.stream.Collectors;

public class HotelImpl implements Hotel {
    private Map<String, Client> clientsMap;
    private Map<String, RoomInfo> rooms;
    private Map<Integer, ReservationInfo> reservations;
    private Map<Integer, Event> seasons;
    private Map<Integer, Event> events;

    private void save(){
        try {
            Persistence.saveRooms(rooms);
            Persistence.saveReservations(reservations);
            Persistence.saveClients(clientsMap);
            Persistence.saveSeasons(seasons);
            Persistence.saveEvents(events);
        } catch (IOException e) {
            System.out.println("Save failed!");
            e.printStackTrace();
        }
    }

    @Override
    public void addClient(String name) {
        if(!clientsMap.containsKey(name)){
            clientsMap.put(name, new Client());
            save();
        }
    }

    @Override
    public void deleteClient(String name) {
        if(clientsMap.containsKey(name)){
            clientsMap.remove(name);
            save();
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
            save();
        } else throw new KeyAlreadyExistsException();
    }

    public RoomInfo getRoom(String name) throws InvalidKeyException {
        if(rooms.containsKey(name)){
            return rooms.get(name);
        } else throw new InvalidKeyException();
    }

    @Override
    public void deleteRoom(String name) throws InvalidKeyException {
        if(rooms.containsKey(name)){
            rooms.remove(name);
            save();
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
        int id = newMapId(reservations.keySet());
        reservations.put(id, request);
        client.reservationIds.add(id);
        if(client.reservationIds.size() >= 5){
            client.discount = true;
        }
        System.out.println(request.getPeriod().getDays());
        save();
        return id;
    }

    private int newMapId(Set<Integer> keys){
        int i = 0;
        while(true){
            if(!keys.contains(i)){
                return i;
            } else {
                i++;
            }
        }
    }

    private int calcReservationCost(ReservationInfo reservation, Client client) {
        int base = reservation
                .getRoomsInfo()
                .stream()
                .mapToInt(RoomInfo::getDailyCost)
                .sum();
        double forPeriod = base * reservation
                .getPeriod()
                .daysList()
                .stream()
                .mapToDouble(this::calcDayModifier)
                .sum();
        if(client.discount){
            return (int) Math.ceil(forPeriod * 0.8);
        } else {
            return (int) Math.ceil(forPeriod);
        }
    }

    private double calcDayModifier(Date date){
        double modifier = 1;
        for (Event event: events.values()) {
            if(event.period.isDayInPeriod(date)){
                modifier = modifier * event.priceModifier;
                break;
            }
        }
        for (Event season: seasons.values()) {
            if(season.period.isDayInPeriod(date)){
                modifier = modifier * season.priceModifier;
                break;
            }
        }
        return modifier;
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
            save();
        }
    }

    private int setPeriodPricing(double modifier, Period period, Map<Integer, Event> category) throws InvalidRequestException {
        List<Event> overlapping = category.values().stream().filter(event -> calcOverlap(period, event.period) != null).collect(Collectors.toList());
        if(overlapping.size() > 0){
            throw new InvalidRequestException();
        } else {
            int id = newMapId(category.keySet());
            category.put(id, new Event(period, modifier));
            save();
            return id;
        }
    }

    private void removePeriodPricing(int id, Map<Integer, Event> category) {
        category.remove(id);
        save();
    }

    private Map<Integer, Event> listPeriodPricing(Map<Integer, Event> category) {
        return category;
    }

    @Override
    public int setSeason(double modifier, Period period) throws InvalidRequestException {
        return setPeriodPricing(modifier, period, seasons);
    }

    @Override
    public void removeSeason(int id) {
        removePeriodPricing(id, seasons);
    }

    @Override
    public Map<Integer, Event> listSeasons() {
        return listPeriodPricing(seasons);
    }

    @Override
    public int setEvent(double modifier, Period period) throws InvalidRequestException {
        return setPeriodPricing(modifier, period, events);
    }

    @Override
    public void removeEvent(int id) {
        removePeriodPricing(id, seasons);
    }

    @Override
    public Map<Integer, Event> listEvents() {
        return listPeriodPricing(seasons);
    }

    private HotelImpl(){
        try {
            try {
                clientsMap = Persistence.loadClients();
            } catch (NoSuchFileException | FileNotFoundException e) {
                clientsMap = new HashMap<>();
            }
            try {
                rooms = Persistence.loadRooms();
            } catch (NoSuchFileException | FileNotFoundException e) {
                rooms = new HashMap<>();
            }
            try {
                reservations = Persistence.loadReservations();
            } catch (NoSuchFileException | FileNotFoundException e) {
                reservations = new HashMap<>();
            }
            try {
                seasons = Persistence.loadSeasons();
            } catch (NoSuchFileException | FileNotFoundException e) {
                seasons = new HashMap<>();
            }
            try {
                events = Persistence.loadEvents();
            } catch (NoSuchFileException | FileNotFoundException e) {
                events = new HashMap<>();
            }
        } catch (IOException e) {
            System.out.println("Failed to load saved data:");
            System.out.println("Exiting.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Hotel instance = new HotelImpl();

    public static Hotel getInstance(){
        return instance;
    }
}
