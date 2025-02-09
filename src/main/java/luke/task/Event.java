package luke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code Event} class represents a task that occurs at a specific time range.
 * This class extends the abstract {@link Task} class and provides functionality
 * for handling the start and end times of an event.
 */
public class Event extends Task {

    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    /**
     * Constructs a new {@code Event} task with the specified name, completion status,
     * start time, and end time. The start and end times are provided as strings in the
     * format "DD/MM/YYYY HH:MM", which are then converted into {@code LocalDateTime}.
     *
     * @param name the name of the event
     * @param isDone the completion status of the event
     * @param start the start time of the event in the format "DD/MM/YYYY HH:MM"
     * @param end the end time of the event in the format "DD/MM/YYYY HH:MM"
     */
    public Event(String name, boolean isDone, String start, String end) {
        super(name, isDone);
        // input format of start and end: [DD/MM/YYYY HH:MM]
        // required format        : [YYYY:MM:DDTHH:MM:SS]
        String startDay = start.substring(0, 2);
        String startMonth = start.substring(3, 5);
        String startYear = start.substring(6, 10);
        String startHour = start.substring(11, 13);
        String startMinute = start.substring(14, 16);
        String startTimeString = String.format("%s-%s-%sT%s:%s:00",
                startYear, startMonth, startDay,
                startHour, startMinute);

        String endDay = end.substring(0, 2);
        String endMonth = end.substring(3, 5);
        String endYear = end.substring(6, 10);
        String endHour = end.substring(11, 13);
        String endMinute = end.substring(14, 16);
        String endTimeString = String.format("%s-%s-%sT%s:%s:00",
                endYear, endMonth, endDay,
                endHour, endMinute);

        this.startTime = LocalDateTime.parse(startTimeString);
        this.endTime = LocalDateTime.parse(endTimeString);
    }

    /**
     * Gets the name of the event.
     *
     * @return the name of the event
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the completion status of the event.
     *
     * @param isDone {@code true} if the event is done, {@code false} otherwise
     */
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Gets the completion status of the event.
     *
     * @return {@code true} if the event is done, {@code false} otherwise
     */
    public boolean getIsDone() {
        return this.isDone;
    }

    /**
     * Gets the start time of the event.
     *
     * @return the start time of the event as a {@code LocalDateTime}
     */
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * Gets the end time of the event.
     *
     * @return the end time of the event as a {@code LocalDateTime}
     */
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /**
     * Returns a string representation of the event, including its completion status,
     * name, start time, and end time.
     *
     * @return a string representation of the event
     */
    @Override
    public String toString() {
        return String.format("[E][%s] %s (from: %s to: %s)", this.isDone ? "X" : " ", this.name,
                this.startTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                this.endTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
    }
}
