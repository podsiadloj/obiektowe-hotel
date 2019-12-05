import javax.management.openmbean.InvalidKeyException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Persistence
{
	public static Map<String, Client> loadClients() throws IOException {
		Map<String, Client> result = new HashMap<>();
		List<List<String>> lines = loadCsv("HotelClients.csv");
		for (List<String> line : lines) {
			if(line.size() < 1) {
				throw new IOException("loadClients: invalid CSV");
			} else {
				String name = line.get(0);
				result.put(name, new Client(
						Boolean.parseBoolean(line.get(1)),
						line.subList(2, line.size()).stream().map(Integer::parseInt).collect(Collectors.toList())
				));
			}
		}
		return result;
	}

	public static Map<String, RoomInfo> loadRooms() throws IOException {
		Map<String, RoomInfo> result = new HashMap<>();
		List<List<String>> lines = loadCsv("HotelRooms.csv");
		for (List<String> line : lines) {
			try {
				if(line.size() < 3) {
					throw new IOException("loadRooms: invalid CSV");
				} else {
					String name = line.get(0);
					int beds = Integer.parseInt(line.get(1));
					Comfort comfort = Comfort.valueOf(line.get(2));
					result.put(name, new RoomInfo(name, beds, comfort));
				}
			} catch (IllegalArgumentException e) {
				throw new IOException("loadRooms: invalid CSV");
			}
		}
		return result;
	}

	public static Map<Integer, ReservationInfo> loadReservations() throws IOException {
		Map<Integer, ReservationInfo> result = new HashMap<>();
		List<List<String>> lines = loadCsv("HotelReservations.csv");
		Hotel hotel = HotelImpl.getInstance();
		for (List<String> line : lines) {
			try {
				if (line.size() < 5) {
					throw new IOException("loadReservations: invalid CSV");
				} else {
					Integer id = Integer.parseInt(line.get(0));
					Date start = Main.parseDate(line.get(1));
					Date end = Main.parseDate(line.get(2));
					Period period = new Period(start, end);
					List<String> rooms = line.subList(4, line.size());
					ReservationInfo reservation = new ReservationInfoImpl(
							period,
							rooms
					);
					reservation.setPrice(Integer.parseInt(line.get(3)));
					result.put(id, reservation);
				}
			}
			catch (InvalidKeyException e) {
				throw new IOException("loadReservations: reservations contain nonexistent rooms");
			} catch (NumberFormatException | ParseException e) {
				throw new IOException("loadReservations: invalid CSV");
			}
		}
		return result;
	}

	private static List<List<String>> loadCsv(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<List<String>> lines = new ArrayList<>();
		while (true)
		{
			String lineStr = bufferedReader.readLine();
//			System.out.println(lineStr);
			if (lineStr == null)
				break;

			if (lineStr.contains(";")){
				lines.add(Arrays.asList(lineStr.split(";")));
			}
		}
		bufferedReader.close();
		return lines;
	}

	public static void saveRooms(Map<String, RoomInfo> rooms) throws IOException {
		List<List<String>> output = new ArrayList<>();
		for (RoomInfo room : rooms.values()) {
			List<String> line = new ArrayList<>();
			line.add(room.name);
			line.add(room.number.toString());
			line.add(room.comfort.toString());
			output.add(line);
		}
		saveToCsv("HotelRooms.csv", output);
	}

	public static void saveReservations(Map<Integer, ReservationInfo> reservations) throws IOException {
		List<List<String>> output = new ArrayList<>();
		for (Integer reservationId : reservations.keySet()) {
			ReservationInfo reservation = reservations.get(reservationId);
			List<String> line = new ArrayList<>();
			line.add(reservationId.toString());
			line.add(Main.dateformat.format(reservation.getPeriod().getStart()));
			line.add(Main.dateformat.format(reservation.getPeriod().getEnd()));
			line.add(reservation.getPrice().toString());
			line.addAll(reservation.getRoomsInfo().stream().map(roomInfo -> roomInfo.name).collect(Collectors.toList()));
			output.add(line);
		}
		saveToCsv("HotelReservations.csv", output);
	}

	public static void saveClients(Map<String, Client> clients) throws IOException {
		List<List<String>> output = new ArrayList<>();
		for (String clientName : clients.keySet()) {
			Client client = clients.get(clientName);
			List<String> line = new ArrayList<>();
			line.add(clientName);
			line.add(Boolean.toString(client.discount));
			line.addAll(client.reservationIds.stream().map(Object::toString).collect(Collectors.toList()));
			output.add(line);
		}
		saveToCsv("HotelClients.csv", output);
	}

	private static void saveToCsv(String filename, List<List<String>> data) throws IOException
	{
		PrintWriter writer  = new PrintWriter(filename, StandardCharsets.UTF_8);

		for (List<String> record : data) {
			String line = String.join(";", record);

			System.out.println(line);

			writer.println(line);
		}

		writer.close();
	}
}
