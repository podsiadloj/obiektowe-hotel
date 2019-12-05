import javax.management.openmbean.InvalidKeyException;
import java.io.*;
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
						Boolean.parseBoolean(line.get(2)),
						line.subList(1, line.size()).stream().map(Integer::parseInt).collect(Collectors.toList())
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
				if (line.size() < 4) {
					throw new IOException("loadReservations: invalid CSV");
				} else {
					Integer id = Integer.parseInt(line.get(0));
					Date start = Main.parseDate(line.get(1));
					Date end = Main.parseDate(line.get(2));
					result.put(id, new ReservationInfoImpl(
							new Period(start, end),
							line.subList(3, line.size()).stream().map(hotel::getRoom).collect(Collectors.toList())
					));
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

	private static void saveToCsv(String filename, List<List<String>> data) throws IOException
	{
		FileWriter fileWriter  = new FileWriter(filename);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		for (List<String> record : data) {
			String line = String.join(";", record);

			System.out.println(line);

			bufferedWriter.write(line);
			bufferedWriter.newLine();
		}

		bufferedWriter.close();
	}

//	static void TestRead()
//	{
//		Map<String, Object> dataMap = new HashMap<String, Object>();
//
//		ReadFromCsv("HotelClients.csv", "Clients", dataMap);
//
//		for (Map.Entry<String, Object> entry : dataMap.entrySet())
//		{
//			String line = entry.getKey() + ";" + entry.getValue();
//
//			System.out.println(line);
//		}
//	}
//
//	static void TestSave()
//	{
//		Map<String, Object> dataMap = new HashMap<String, Object>();
//
//		dataMap.put(UUID.randomUUID().toString(), new Client(Client.Sex.Female, "Klaudia", "Glabala", "kg@portal.com", "601602603"));
//		dataMap.put(UUID.randomUUID().toString(), new Client(Client.Sex.Male, "Jan", "Podsiadlo", "jp@portal.com", "701702703"));
//
//		SaveToCsv("HotelClients.csv", dataMap);
//	}
}
