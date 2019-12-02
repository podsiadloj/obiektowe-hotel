import javax.management.openmbean.InvalidKeyException;
import javax.management.openmbean.KeyAlreadyExistsException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Date parseDate(String ds) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
        return df.parse(ds);
    }

    public static void main(String[] args) {
        // interfejs dla recepcjonisty: rolę użytkownika dla klienta dodamy jeśli starczy czasu, z osobnym interfejsem
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        boolean end = false;
        while(!end){ //pętla REPL
            System.out.println("Enter command ('help' for help):");
            try {
                String rawInput = scanner.next();
                List<String> input = parseRawInput(rawInput);
                String command = input.get(0);
                switch (command) {
                    case "help":
                        printHelp();
                        break;
                    case "addclient":
                        addclient(input);
                        break;
                    case "deleteclient":
                        deleteclient(input);
                        break;
                    case "showclient":
                        showclient(input);
                        break;
                    case "listclients":
                        listclients();
                        break;
                    case "addroom":
                        addroom(input);
                        break;
                    case "deleteroom":
                        deleteroom(input);
                        break;
                    case "freerooms":
                        freerooms(input);
                        break;
                    case "addreservation":
                        addreservation(input);
                        break;
                    case "listreservations":
                        listreservations();
                        break;
                    case "showreservation":
                        showreservation(input);
                        break;
                    case "deletereservation":
                        deletereservation(input);
                        // parametr: id (Integer)
                        break;
                    // mniejszy priorytet - dopisać jak będzie czas: ustawianie okresów, w których cena jest wyższa/niższa
                    case "quit":
                        end = true;
                        break;
                    default:
                        syntaxError();
                }
            } catch (ParseException | IllegalArgumentException e) {
                syntaxError();
            }
        }
    }

    private static List<String> parseRawInput(String rawInput) {
        String[] quoted = rawInput.split("\"", -1);
        if(quoted.length%2 == 1){
            List<String> splitOnSpaces = new ArrayList<>();
            for (int i = 0; i < quoted.length; i++) {
                if(i%2 == 1){
                    splitOnSpaces.add(quoted[i]);
                } else {
                    splitOnSpaces.addAll(Arrays.asList(quoted[i].split("\\s+")));
                }
            }
            return splitOnSpaces.stream().filter(s -> (s.equals("") || s.matches("^\\s+$"))).collect(Collectors.toList());
        } else throw new IllegalArgumentException();
    }

    private static void syntaxError() {
        System.out.println("Syntax error");
        printHelp();
    }

    private static void printHelp() {
        System.out.println("addclient [name] - create a new customer");
        System.out.println("deleteclient [name] - delete a customer");
        System.out.println("showclient [name] - print customer info");
        System.out.println("listclients - print all customers");
        System.out.println("showclient [name] - print customer info");
        System.out.println("addroom [name] [number of beds: integer] [comfort: 'HIGH'/'MEDIUM'/'LOW'] - create a new room entry");
        System.out.println("deleteroom [name] - delete a room");
        System.out.println("freerooms [start: dd.mm.yyyy] [end: dd.mm.yyyy] [size of room 1] [size of room 2]... - find all matching rooms");
        System.out.println("addreservation [start: dd.mm.yyyy] [end: dd.mm.yyyy] [client name] [name of room 1] [name of room 2]... - create a reservation and calculate the cost");
        System.out.println("listreservations - list all reservations");
        System.out.println("showreservation [id: integer] - show detailed info about reservation");
        System.out.println("deletereservation [id: integer] - delete reservation");
    }

    private static void showreservation(List<String> input) {
        // informacje o rezerwacji
        // parametr id (Integer)
        // wypisywane:
        // okres
        // klient
        // lista pokojów
        int id = Integer.parseInt(input.get(1));
        ReservationInfo reservation = HotelImpl.getInstance().getReservation(id);
        if (reservation == null) {
            System.out.println("No such reservation");
        } else {
            printReservation(id, reservation, true);
        }
    }

    private static void deletereservation(List<String> input) {
        int id = Integer.parseInt(input.get(1));
        ReservationInfo reservation = HotelImpl.getInstance().getReservation(id);
        if (reservation == null) {
            System.out.println("No such reservation");
        } else {
            HotelImpl.getInstance().deleteReservation(id);
        }
    }

    // lista rezerwacji
    // usprawnienia z niższym priorytetem: filtrowanie rezerwacji po okresie, kliencie, pokojach
    private static void listreservations() {
        List<Map.Entry<Integer, ReservationInfo>> entries = HotelImpl.getInstance().listReservations();
        for (Map.Entry<Integer, ReservationInfo> entry : entries) {
            printReservation(entry.getKey(), entry.getValue(), false);
        }
    }

    // dodaj rezerwację
    // parametry:
    // nazwa klienta (String)
    // okres od (String parsowalny do daty)
    // okres do (String parsowalny do daty)
    // pokoje (lista Stringów nazw pokojów)
    //
    // wypisuje cenę rezerwacji
    private static void addreservation(List<String> input) throws ParseException {
        if(input.size() > 4) {
            try {
                Hotel hotel = HotelImpl.getInstance();
                Client client = hotel.getClient(input.get(3));
                if(client != null) {
                    Period period = new Period(parseDate(input.get(1)), parseDate(input.get(2)));
                    List<RoomInfo> rooms = input
                            .subList(4, input.size())
                            .stream()
                            .map(hotel::getRoom)
                            .collect(Collectors.toList());
                    ReservationInfo reservation = new ReservationInfoImpl(period, rooms);
                    int price = hotel.makeReservation(client, reservation);
                    System.out.println("Reservation created, price " + (double)price/100 + " PLN");
                }
            } catch (InvalidKeyException e) {
                System.out.println("Rooms list invalid");
            } catch (Hotel.InvalidRequestException e) {
                System.out.println("Invalid reservation request");
            }
        } else syntaxError();
    }

    // lista pokojów
    // parametry:
    // okres od (String parsowalny do daty)
    // okres do (String parsowalny do daty)
    // pokoje (lista Integerów)
    private static void freerooms(List<String> input) throws ParseException {
        if(input.size() > 3) {
            Period period = new Period(parseDate(input.get(1)), parseDate(input.get(2)));
            List<Integer> roomSizes = input.subList(3, input.size()).stream().map(Integer::parseInt).collect(Collectors.toList());
            Map<String, RoomInfo> rooms = HotelImpl.getInstance().findFreeRooms(period, roomSizes);
            if(rooms.size() > 0){
                for (RoomInfo room: rooms.values()) {
                    printRoom(room);
                }
            } else {
                System.out.println("No rooms found");
            }
        } else syntaxError();
    }

    // dodaj pokój
    // parametry:
    // nazwa
    // liczba miejsc
    // komfort (HIGH, MEDIUM, LOW)
    private static void addroom(List<String> input) {
        if(input.size() == 4){
            try {
                int beds = Integer.parseInt(input.get(2));
                Comfort comfort = Comfort.valueOf(input.get(3).toUpperCase());
                HotelImpl.getInstance().addRoom(input.get(1), beds, comfort);
                System.out.println("Room created");
            } catch(KeyAlreadyExistsException e) {
                System.out.println("Room already exists");
            }
        } else {
            syntaxError();
        }
    }

    // usuń pokój
    // parametr: nazwa
    private static void deleteroom(List<String> input) {
        String name = input.get(1);
        try {
            HotelImpl.getInstance().deleteRoom(name);
        } catch (InvalidKeyException e) {
            System.out.println("Room doesn't exist");
        }
    }

    private static void listclients() {
        List<String> clients = HotelImpl.getInstance().clients();
        for (String client : clients) {
            System.out.println(client);
        }
        if(clients.size() == 0){
            System.out.println("No clients");
        }
    }

    // parametr: nazwa (String)
    // dane klienta:
    // dotychczasowe rezerwacje
    // czy stały klient (true/false) - stały klient ustawiany automatycznie po 5 rezerwacjach
    private static void showclient(List<String> input) {
        String name = input.get(1);
        Client client = HotelImpl.getInstance().getClient(name);
        if(client != null){
            printClient(name, client);
        } else System.out.println("No such client");
    }

    private static void printClient(String name, Client client) {
        System.out.println("name: " + name);
        System.out.println("Frequent client discount: " + client.discount);
        System.out.println("Previous reservations: ");
        for (int id : client.reservationIds) {
            printReservation(id, HotelImpl.getInstance().getReservation(id), false);
        }
        if(client.reservationIds.size() == 0){
            System.out.println("  None");
        }
    }

    private static void printReservation(int id, ReservationInfo reservation, boolean detailed){
        System.out.print(id + ": " + reservation.getPrice()/100 + "PLN");
        if(detailed){
            System.out.println();
            System.out.println("  Start:" + reservation.getPeriod().getStart().toString());
            System.out.println("  End:  " + reservation.getPeriod().getEnd().toString());
            System.out.println("  Rooms:  " + reservation.getPeriod().getEnd().toString());
            for (RoomInfo room: reservation.getRoomsInfo()) {
                printRoom(room);
            }
        } else {
            System.out.println(", " + reservation.getPeriod().getDays() + " days");
        }

    }

    private static void printRoom(RoomInfo room){
        System.out.println("    Room '" + room.name + "':");
        System.out.println("      Beds: " + room.number);
        System.out.println("      Comfort: " + room.comfort);
    }

    // dodanie klienta
    // parametr: nazwa (String)
    private static void addclient(List<String> input) {
        System.out.println(input);
        String name = input.get(1);
        if(HotelImpl.getInstance().getClient(name) == null){
            HotelImpl.getInstance().addClient(name);
            System.out.println("Client added");
        } else {
            System.out.println("Client already exists");
        }
    }

    // usuwanie klienta
    // parametr: nazwa (String)
    private static void deleteclient(List<String> input) {
        String name = input.get(1);
        if(HotelImpl.getInstance().getClient(name) == null){
            System.out.println("Client does not exist");
        } else {
            HotelImpl.getInstance().deleteClient(name);
            System.out.println("Client removed");
        }
    }
}
