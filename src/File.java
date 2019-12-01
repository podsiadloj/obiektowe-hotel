import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class File
{
	static boolean ReadFromCsv(String fileName, String dataType, Map<String, Object> dataMap)
	{
		try
		{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;

			while (true)
			{
				line = bufferedReader.readLine();

				if (line == null)
					break;

				System.out.println(line);

				int position = line.indexOf(";");

				if (position > -1)
				{
					String key = line.substring(0, position);
					String[] data = line.substring(position + 1).split(";");
					Object value = null;

					switch (dataType)
					{
						case "Clients":
							value = new Client(Client.Sex.valueOf(data[0]), data[1], data[2], data[3], data[4]);
							break;
//						case "Reservations":
//							value = new Reservation(data[0], data[1], data[2], ...);
//							break;
//						case "Rooms":
//							value = new Room(data[0], data[1], data[2], ...);
//							break;
					}

					if (value != null)
						dataMap.put(key, value);
				}
			}

			bufferedReader.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();

			return false;
		}

		return true;
	}

	static boolean SaveToCsv(String fileName, Map<String, Object> dataMap)
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
