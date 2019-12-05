import javax.management.openmbean.InvalidKeyException;
import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.*;
import java.util.*;

public interface Hotel {

    void addClient(String name);
    void deleteClient(String name);
    List<String> clients();
    Client getClient(String name);

    void addRoom(String name, int nOfBeds, Comfort comfort) throws KeyAlreadyExistsException;
    void deleteRoom(String name) throws InvalidKeyException;
    RoomInfo getRoom(String name) throws InvalidKeyException;

    // rooms jest listą liczb określających ile osób chcemy zakwaterować w pokoju
    // np.: { 1, 2} oznacza, że potrzebujemy pokoju dla jednej osoby i drugiego pokoju dla dwu osób.
    Map<String, RoomInfo> findFreeRooms(Period period, List<Integer> rooms);

    // zwraca cenę w groszach
    int makeReservation(Client client,  ReservationInfo request) throws InvalidRequestException;
    List<Map.Entry<Integer, ReservationInfo>> listReservations();
    ReservationInfo getReservation(int id);
    void deleteReservation(int id);

    //seasons can't overlap with other seasons

    int setSeason(int modifier, Period period);
    void removeSeason(int id);
    Map<Integer, Event> listSeasons();

    // events can't overlap with other events, but can with seasons

    int setEvent(int modifier, Period period);
    void removeEvent(int id);
    Map<Integer, Event> listEvents();

    class InvalidRequestException extends Exception {}

}
