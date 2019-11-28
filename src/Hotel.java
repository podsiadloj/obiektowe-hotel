import java.io.*;
import java.util.*;

public interface Hotel {

    void addClient(String name);
    void deleteClient(String name);
    List<Client> clients();

    void addRoom(String name, int nOfBeds, Comfort comfort);
    void deleteRoom(String name);

    // rooms jest listą liczb określających ile osób chcemy zakwaterować w pokoju
    // np.: { 1, 2} oznacza, że potrzebujemy pokoju dla jednej osoby i drugiego pokoju dla dwu osób.
    List<ReservationInfo> findFreeRooms(Period period, List<Integer> rooms);

    // zwraca cenę w groszach
    int makeReservation(Client client,  ReservationInfo request);
    List<ReservationInfo> listReservations();
    ReservationInfo getReservation(int id);
    void deleteReservation(int id);

}
