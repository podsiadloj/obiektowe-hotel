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
}
