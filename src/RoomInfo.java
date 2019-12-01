//nazwa pokoju i ilość ludzi w nim zakwaterowanych
public class RoomInfo {
    public String name;
    public Integer number;
    public Comfort comfort;

    public RoomInfo(String name, Integer number, Comfort comfort){
        this.name = name;
        this.number = number;
        this.comfort = comfort;
    }

    public int getDailyCost(){
        int base = 3000 + (3000 * number);
        switch (comfort){
            case HIGH:
                return base * 2;
            case MEDIUM:
                return (int) Math.ceil(base * 1.5);
            case LOW:
                return base;
            default:
                throw new IllegalStateException("Unexpected value: " + comfort);
        }
    }
}
