package duke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// abstract Task class
public static abstract class Task {
    protected String name;
    protected boolean isDone;

    public Task(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    public abstract String getName();

    public abstract void setIsDone(boolean isDone);

    public abstract boolean getIsDone();

    @Override
    public abstract String toString();
}

// Task subtypes
public static class ToDo extends Task {

    protected String dueDate;

    public ToDo(String name, boolean isDone) {
        super(name, isDone);
    }

    public String getName() { return this.name; }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean getIsDone() { return isDone; }

    @Override
    public String toString() {
        return String.format("[T][%s] %s", this.isDone?"X":" ", this.name);
    }
}

public static class Deadline extends Task {

    protected LocalDateTime dueTime;

    public Deadline(String name, boolean isDone, String dueTime) {
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

public static class Event extends Task {

    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Event(String name, boolean isDone, String start, String end) {
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