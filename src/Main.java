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
                case "showclient":
                    // parametr: nazwa (String)
                    // dane klienta:
                    // dotychczasowe rezerwacje
                    // czy stały klient (true/false) - stały klient ustawiany automatycznie po 5 rezerwacjach
                case "listclients":
                    // lista klientów (bez filtrowania)
                    break;
                case "addroom":
                    // dodaj pokój
                    // parametry:
                    // nazwa
                    // liczba miejsc
                    // komfort (HIGH, MEDIUM, LOW)
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
