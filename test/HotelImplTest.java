import org.junit.jupiter.api.*;

import javax.management.openmbean.InvalidKeyException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HotelImplTest
{
	@BeforeAll
	static void Clean()
	{
		Hotel hotel = HotelImpl.getInstance();

		for (Map.Entry<Integer, ReservationInfo> reservation : hotel.listReservations())
		{
			hotel.deleteReservation(reservation.getKey());
		}

		try
		{
			hotel.deleteClient("Client");
		}
		catch(Exception e)
		{
		}

		try
		{
			hotel.deleteRoom("Room1");
		}
		catch(Exception e)
		{
		}

		try
		{
			hotel.deleteRoom("Room2");
		}
		catch(Exception e)
		{
		}

		try
		{
			hotel.deleteRoom("Room3");
		}
		catch(Exception e)
		{
		}
	}

	@Order(1)
	@Test
	void addClient()
	{
		Hotel hotel = HotelImpl.getInstance();

		hotel.addClient("Client");

		assertEquals(hotel.clients().size(), 1);
	}

	@Order(2)
	@Test
	void clients()
	{
		Hotel hotel = HotelImpl.getInstance();

		assertNotNull(hotel.clients());
	}

	@Order(3)
	@Test
	void getClient()
	{
		Hotel hotel = HotelImpl.getInstance();

		assertEquals(hotel.clients().size(), 1);
		assertNotNull(hotel.getClient("Client"));
	}

	@Order(4)
	@Test
	void deleteClient()
	{
		Hotel hotel = HotelImpl.getInstance();

		assertEquals(hotel.clients().size(), 1);

		hotel.deleteClient("Client");

		assertEquals(hotel.clients().size(), 0);
	}

	@Order(5)
	@Test
	void addRoom()
	{
		Hotel hotel = HotelImpl.getInstance();

		hotel.addRoom("Room1", 1, Comfort.LOW);

		assertNotNull(hotel.getRoom("Room1"));
		assertThrows(InvalidKeyException.class, () -> {hotel.getRoom("Room2");});
	}

	@Order(6)
	@Test
	void getRoom()
	{
		Hotel hotel = HotelImpl.getInstance();

		RoomInfo room = hotel.getRoom("Room1");

		assertEquals(room.name, "Room1");
		assertEquals(room.number, 1);
		assertEquals(room.comfort, Comfort.LOW);
	}

	@Order(7)
	@Test
	void deleteRoom()
	{
		Hotel hotel = HotelImpl.getInstance();

		hotel.deleteRoom("Room1");

		assertThrows(InvalidKeyException.class, () -> {hotel.getRoom("Room1");});
	}

	@Order(8)
	@Test
	void findFreeRooms()
	{
		Hotel hotel = HotelImpl.getInstance();

		hotel.addRoom("Room1", 1, Comfort.MEDIUM);

		LocalDate date = LocalDate.now();
		Period period = new Period(Date.valueOf(date.plusDays(10)), Date.valueOf(date.plusDays(20)));
		List<Integer> rooms = new ArrayList<Integer>(Arrays.asList(1));
		Map<String, RoomInfo> freeRooms = hotel.findFreeRooms(period, rooms);

		assertEquals(freeRooms.size(), 1);
		assertTrue(freeRooms.keySet().contains("Room1"));
	}

	@Order(9)
	@Test
	void makeReservation()
	{
		Hotel hotel = HotelImpl.getInstance();

		hotel.addClient("Client");

		Client client = hotel.getClient("Client");
		RoomInfo room1 = hotel.getRoom("Room1");

		LocalDate date = LocalDate.now();
		Period period = new Period(Date.valueOf(date.plusDays(10)), Date.valueOf(date.plusDays(20)));
		List<String> rooms = new ArrayList<String>(Arrays.asList("Room1"));
		final ReservationInfo reservation1 = new ReservationInfoImpl(period, rooms);

		assertEquals(hotel.listReservations().size(), 0);
		assertDoesNotThrow(() -> {hotel.makeReservation(client, reservation1);});
		assertEquals(hotel.listReservations().size(), 1);

		Period period2 = new Period(Date.valueOf(date.plusDays(15)), Date.valueOf(date.plusDays(25)));
		final ReservationInfo reservation2 = new ReservationInfoImpl(period2, rooms);

		assertThrows(Hotel.InvalidRequestException.class, () -> {hotel.makeReservation(client, reservation2);});
		assertEquals(hotel.listReservations().size(), 1);
	}

	@Order(10)
	@Test
	void listReservations() throws Hotel.InvalidRequestException
	{
		Hotel hotel = HotelImpl.getInstance();

		assertEquals(hotel.listReservations().size(), 1);
	}

	@Order(11)
	@Test
	void getReservation() throws Hotel.InvalidRequestException
	{
		Hotel hotel = HotelImpl.getInstance();

		hotel.addRoom("Room2", 2, Comfort.MEDIUM);

		Client client = hotel.getClient("Client");
		RoomInfo room2 = hotel.getRoom("Room2");

		LocalDate date = LocalDate.now();
		Period period = new Period(Date.valueOf(date.plusDays(15)), Date.valueOf(date.plusDays(25)));
		List<String> rooms = new ArrayList<String>(Arrays.asList("Room2"));
		ReservationInfo reservation = new ReservationInfoImpl(period, rooms);

		final int id = hotel.makeReservation(client, reservation);

		assertDoesNotThrow(() -> {hotel.getReservation(id);});
		assertEquals(hotel.listReservations().size(), 2);
	}

	@Order(12)
	@Test
	void deleteReservation() throws Hotel.InvalidRequestException
	{
		Hotel hotel = HotelImpl.getInstance();

		hotel.addRoom("Room3", 3, Comfort.HIGH);

		Client client = hotel.getClient("Client");
		RoomInfo room1 = hotel.getRoom("Room1");
		RoomInfo room2 = hotel.getRoom("Room2");
		RoomInfo room3 = hotel.getRoom("Room3");

		LocalDate date = LocalDate.now();
		Period period = new Period(Date.valueOf(date.plusDays(30)), Date.valueOf(date.plusDays(40)));
		List<String> rooms = new ArrayList<String>(Arrays.asList("Room1", "Room2", "Room3"));
		ReservationInfo reservation = new ReservationInfoImpl(period, rooms);

		assertEquals(hotel.listReservations().size(), 2);

		int id = hotel.makeReservation(client, reservation);

		assertEquals(hotel.listReservations().size(), 3);

		hotel.deleteReservation(id);

		assertEquals(hotel.listReservations().size(), 2);
	}
}
