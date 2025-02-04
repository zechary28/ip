package luke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventTest extends TaskTest {

    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public EventTest(String name, boolean isDone, String start, String end) {
        super(name, isDone);
        // input format of dueTime: [DD/MM/YYYY HH:MM]
        // required format        : [YYYY:MM:DDTHH:MM:SS]
        String startDay = start.substring(0, 2), startMonth = start.substring(3, 5), startYear = start.substring(6, 10);
        String startHour = start.substring(11, 13), startMinute = start.substring(14, 16);
        String startTimeString = String.format("%s-%s-%sT%s:%s:00", startYear, startMonth, startDay, startHour, startMinute);
        String endDay = end.substring(0, 2), endMonth = end.substring(3, 5), endYear = end.substring(6, 10);
        String endHour = end.substring(11, 13), endMinute = end.substring(14, 16);
        String endTimeString = String.format("%s-%s-%sT%s:%s:00", endYear, endMonth, endDay, endHour, endMinute);
        this.startTime = LocalDateTime.parse(startTimeString);
        this.endTime = LocalDateTime.parse(endTimeString);
    }

    public String getName() { return this.name; }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean getIsDone() { return this.isDone; }

    public LocalDateTime getStartTime() { return this.startTime; }

    public LocalDateTime getEndTime() { return this.endTime; }

    @Override
    public String toString() {
        return String.format("[E][%s] %s (from: %s to: %s)", this.isDone?"X":" ", this.name
                , this.startTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                , this.endTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
    }
}