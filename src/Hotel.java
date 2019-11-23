import java.io.*;
import java.util.*;

public interface Hotel {
    void loadRooms(Reader reader);
    void saveRooms(Writer writer);

    void addRoom(String name, int nOfBeds);
    void deleteRoom(String name);

    // rooms jest listą liczb określających ile osób chcemy zakwaterować w pokoju
    // np.: { 1, 2} oznacza, że potrzebujemy pokoju dla jednej osoby i drugiego pokoju dla dwu osób.
    List<ReservationInfo> findFreeRooms(Period period, List<Integer> rooms);
    boolean makeReservation(Client client,  ReservationInfo request);
}
