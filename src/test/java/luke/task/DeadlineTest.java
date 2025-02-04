package luke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeadlineTest extends TaskTest {

    protected LocalDateTime dueTime;

    public DeadlineTest(String name, boolean isDone, String dueTime) {
        super(name, isDone);
        // input format of dueTime: [DD/MM/YYYY HH:MM]
        // required format        : [YYYY:MM:DDTHH:MM:SS]
        String day = dueTime.substring(0, 2), month = dueTime.substring(3, 5), year = dueTime.substring(6, 10);
        String hour = dueTime.substring(11, 13), minute = dueTime.substring(14, 16);
        String timeString = String.format("%s-%s-%sT%s:%s:00", year, month, day, hour, minute);
        this.dueTime = LocalDateTime.parse(timeString); // assume string is in correct format
    }

    public String getName() { return this.name; }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public LocalDateTime getDueTime() { return this.dueTime; }

    public boolean getIsDone() { return isDone; }

    @Override
    public String toString() {
        return String.format("[D][%s] %s (by: %s)", this.isDone?"X":" "
                , this.name, this.dueTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
    }
}