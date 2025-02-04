package luke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventStub extends TaskTest {

    public EventStub(String name, boolean isDone, String start, String end) {
        super(name, isDone);
    }

    public String getName() { return "EventStub"; }

    public void setIsDone(boolean isDone) { }

    public boolean getIsDone() { return true; }

    public LocalDateTime getStartTime() { return LocalDateTime.parse("2025-01-01T01:01:01"); }

    public LocalDateTime getEndTime() { return LocalDateTime.parse("2025-02-02T02:02:02"); }

    @Override
    public String toString() {
        return "[E][1] EventStub (from: 01 Jan 2025 to: 02 Feb 2025)";
    }
}