import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Okres rezerwacji od-do
public class Period {
    public static final int DAY_MS = (1000 * 60 * 60 * 24);
    private Date start;
    private Date end;

    public Period(Date start, Date end) throws IllegalArgumentException {
        this.start = start;
        this.end = end;
        if(getDays() < 1){
            throw new IllegalArgumentException();
        }
    }

    public int getDays(){
        return (int) Math.floor( (double) (end.getTime() - start.getTime()) / DAY_MS) + 1;
    }

    public List<Date> daysList(){
        long endTime = end.getTime();
        List<Date> result = new ArrayList<>();
        for(long i = start.getTime(); i <= endTime; i += DAY_MS){
            result.add(new Date(i));
        }
        return result;
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
