import java.util.Date;

//Okres rezerwacji od-do
public class Period {
    private Date start;
    private Date end;

    public Period(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public int getDays(){
        return (int) Math.ceil( (double) (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
