package luke.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code Deadline} class represents a task with a specific deadline.
 * This class extends the abstract {@link Task} class and adds functionality
 * for handling a due time for the task.
 */
public class Deadline extends Task {

    protected LocalDateTime dueTime;

    /**
     * Constructs a new {@code Deadline} task with the specified name, completion status,
     * and due time. The due time is provided as a string in the format "DD/MM/YYYY HH:MM",
     * which is then converted into a {@code LocalDateTime}.
     *
     * @param name the name of the task
     * @param isDone the completion status of the task
     * @param dueTime the due time of the task in the format "DD/MM/YYYY HH:MM"
     */
    public Deadline(String name, boolean isDone, String dueTime) {
        super(name, isDone);
        // input format of dueTime: [DD/MM/YYYY HH:MM]
        // required format        : [YYYY:MM:DDTHH:MM:SS]
        String day = dueTime.substring(0, 2);
        String month = dueTime.substring(3, 5);
        String year = dueTime.substring(6, 10);
        String hour = dueTime.substring(11, 13);
        String minute = dueTime.substring(14, 16);
        String timeString = String.format("%s-%s-%sT%s:%s:00", year, month, day, hour, minute);
        this.dueTime = LocalDateTime.parse(timeString); // assume string is in correct format
    }

    /**
     * Gets the name of the task.
     *
     * @return the name of the task
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the completion status of the task.
     *
     * @param isDone {@code true} if the task is done, {@code false} otherwise
     */
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Gets the due time of the task.
     *
     * @return the due time of the task as a {@code LocalDateTime}
     */
    public LocalDateTime getDueTime() {
        return this.dueTime;
    }

    /**
     * Gets the completion status of the task.
     *
     * @return {@code true} if the task is done, {@code false} otherwise
     */
    public boolean getIsDone() {
        return isDone;
    }

    /**
     * Returns a string representation of the deadline task, including its completion
     * status, name, and formatted due time.
     *
     * @return a string representation of the deadline task
     */
    @Override
    public String toString() {
        return String.format("[D][%s] %s (by: %s)", this.isDone ? "X" : " ",
                this.name, this.dueTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
    }

    /**
     * Compares this deadline task to another task for ordering.
     *
     * The comparison is performed based on the following criteria:
     * 1. If the other task is an instance of ToDo, this deadline task is considered larger
     *    (ToDo < Deadline < Event < everything else).
     * 2. If the other task is also an instance of Deadline:
     *    - First, compare their "done" statuses. Tasks that are not done come before
     *      tasks that are done.
     *    - If the "done" statuses are the same, compare their due times using
     *      the dueTime property.
     * 3. For all other task types, this deadline task is considered smaller
     *    (ToDo < Deadline < Event < everything else).
     *
     * @param t the task to be compared with this deadline task.
     * @return a negative integer, zero, or a positive integer as this deadline task
     *         is less than, equal to, or greater than the specified task.
     * @throws NullPointerException if the specified task is null.
     */
    public int compareTo(Task t) {
        if (t instanceof ToDo) { // todo < deadline < event < everything else
            return 1;
        } else if (t instanceof Deadline) { // sort by time
            if (this.isDone != t.isDone) {
                return this.isDone ? 1 : -1;
            } else {
                Deadline dl = (Deadline) t;
                return this.dueTime.compareTo(dl.dueTime);
            }
        } else { // todo < deadline < event < everything else
            return -1;
        }
    }
}
