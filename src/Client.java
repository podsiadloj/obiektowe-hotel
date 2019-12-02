import java.util.*;

public class Client {
    public boolean discount;
    public List<Integer> reservationIds;


    public Client(){
        discount = false;
        reservationIds = new ArrayList<>();
    }

    public Client(boolean discount, List<Integer> reservationIds){
        this.discount = discount;
        this.reservationIds = reservationIds;
    }
}
