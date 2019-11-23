//Okres rezerwacji od-do
public class Period {
    private Integer start;
    private Integer end;

    public Period(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }
}
