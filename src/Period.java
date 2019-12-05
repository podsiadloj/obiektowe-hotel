import java.util.Date;
import java.util.List;

//Okres rezerwacji od-do
public class Period {
    private Date start;
    private Date end;

    public Period(Date start, Date end) {
        this.start = start;
        this.end = end;
        if(getDays() < 1){
            throw new IllegalArgumentException();
        }
    }

    public int getDays(){
        return (int) Math.floor( (double) (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24)) + 1;
    }

    public boolean isDayInPeriod(Date day){
        return day.getTime() >= start.getTime() && day.getTime() <= end.getTime(); //good enough since we always record dates with day precision and the same timezone
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
