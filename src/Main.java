import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Hotel hotel = new HotelImpl();

        // interfejs dla recepcjonisty: rolę użytkownika dla klienta dodamy jeśli starczy czasu, z osobnym interfejsem

        // trzeba zmienić Hotel żeby pozwalał na wykonanie poniższych akcji
        Scanner scanner = new Scanner(System.in);
        boolean end = false;
        while(!end){ //pętla REPL
            String input = scanner.next();
            String command = input.split(" ")[0];
            switch (command) {
                case "help":
                    // print help
                    break;
                case "addclient":
                    // dodanie klienta
                    // parametr: nazwa (String)
                    break;
                case "deleteclient":
                    // usuwanie klienta
                    // parametr: nazwa (String)
                    break;
                case "listclients":
                    // lista klientów z ich danymi (bez filtrowania)
                    // dane klienta:
                    // nazwa (string)
                    // liczba dotychczasowych rezerwacji (int)
                    // czy stały klient (true/false)
                    break;
                case "addroom":
                    // dodaj pokój
                    // parametry:
                    // nazwa
                    // liczba miejsc
                    // komfort (1, 2, 3)
                    break;
                case "deleteroom":
                    // usuń pokój
                    // parametr: nazwa
                    break;
                case "freerooms":
                    // lista pokojów
                    // parametry:
                    // okres od (String parsowalny do daty)
                    // okres do (String parsowalny do daty)
                    // pokoje (lista Integerów)
                    break;
                case "addreservation":
                    // dodaj rezerwację
                    // parametry:
                    // nazwa klienta (String)
                    // okres od (String parsowalny do daty)
                    // okres do (String parsowalny do daty)
                    // pokoje (lista Stringów nazw pokojów)
                    //
                    // wypisuje cenę rezerwacji
                    break;
                case "listreservations":
                    // lista rezerwacji
                    // usprawnienia z niższym priorytetem: filtrowanie rezerwacji po okresie, kliencie, pokojach
                    break;
                case "showreservation":
                    // informacje o rezerwacji
                    // parametr id (Integer)
                    // wypisywane:
                    // okres
                    // klient
                    // lista pokojów
                case "deletereservation":
                    // parametr: id (Integer)
                    break;
                case "quit":
                    end = true;
                    break;
                // mniejszy priorytet - dopisać jak będzie czas: ustawianie okresów, w których cena jest wyższa/niższa
                default:
                    System.out.println("Invalid command");
                    // print help
            }
        }
    }
}
