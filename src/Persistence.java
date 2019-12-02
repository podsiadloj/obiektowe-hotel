import java.io.*;
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

	static boolean saveToCsv(String fileName, Map<String, Object> dataMap)
	{
		try
		{
			FileWriter fileWriter  = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (Map.Entry<String, Object> entry : dataMap.entrySet())
			{
				String line = entry.getKey() + ";" + entry.getValue();

				System.out.println(line);

				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();

			return false;
		}

		return true;
	}

	static void TestRead()
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();

		ReadFromCsv("HotelClients.csv", "Clients", dataMap);

		for (Map.Entry<String, Object> entry : dataMap.entrySet())
		{
			String line = entry.getKey() + ";" + entry.getValue();

			System.out.println(line);
		}
	}

	static void TestSave()
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put(UUID.randomUUID().toString(), new Client(Client.Sex.Female, "Klaudia", "Glabala", "kg@portal.com", "601602603"));
		dataMap.put(UUID.randomUUID().toString(), new Client(Client.Sex.Male, "Jan", "Podsiadlo", "jp@portal.com", "701702703"));

		SaveToCsv("HotelClients.csv", dataMap);
	}
}
