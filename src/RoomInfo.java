//nazwa pokoju i ilość ludzi w nim zakwaterowanych
public class RoomInfo {
    private String name;
    private Integer number;

    public RoomInfo(String name, Integer number, Comfort comfort){
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }
}
