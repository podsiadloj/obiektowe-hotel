import java.util.*;

public class Client
{
	public enum Sex
	{
		Unknown,
		Female,
		Male
	}
//	public boolean discount;
//	public List<Integer> reservationIds;
	public Sex sex;
	public String name;
	public String surname;
	public String email;
	public String phone;

	public Client()
	{
//		discount = false;
//		reservationIds = new ArrayList<>();
		this.sex = Sex.Unknown;
	}

//	public Client(boolean discount, List<Integer> reservationIds){
//		this.discount = discount;
//		this.reservationIds = reservationIds;
//	}

	public Client(Sex sex, String name, String surname, String email, String phone)
	{
		this.sex = sex;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
	}

	@Override
	public String toString()
	{
		return String.format("%1$s;%2$s;%3$s;%4$s;%5$s", this.sex, this.name, this.surname, this.email, this.phone);
	}
}
