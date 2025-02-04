package luke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeadlineStub extends TaskTest {

    public DeadlineStub(String name, boolean isDone, String dueTime) {
        super(name, isDone);
    }

    public String getName() { return "DeadlineStub"; }

    public void setIsDone(boolean isDone) {
    }

    public LocalDateTime getDueTime() { return LocalDateTime.parse("2025-01-01T01:01:01"); }

    public boolean getIsDone() { return true; }

    @Override
    public String toString() {
        return "[D][1] DeadlineStub (by: 01 Jan 2025)";
    }
}