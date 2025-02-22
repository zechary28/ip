package luke.task;

/**
 * The {@code ToDo} class represents a task without a specific due date.
 * This class extends the abstract {@link Task} class and provides functionality
 * for managing a task's completion status and name.
 */
public class ToDo extends Task {

    protected String dueDate;

    /**
     * Constructs a new {@code ToDo} task with the specified name and completion status.
     *
     * @param name the name of the task
     * @param isDone the completion status of the task
     */
    public ToDo(String name, boolean isDone) {
        super(name, isDone);
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
     * Gets the completion status of the task.
     *
     * @return {@code true} if the task is done, {@code false} otherwise
     */
    public boolean getIsDone() {
        return isDone;
    }

    /**
     * Returns a string representation of the todo task, including its completion
     * status and name.
     *
     * @return a string representation of the todo task
     */
    @Override
    public String toString() {
        return String.format("[T][%s] %s", this.isDone ? "X" : " ", this.name);
    }

    /**
     * Compares this todo task to another task for ordering.
     *
     * The comparison is performed based on the following criteria:
     * 1. If the other task is an instance of ToDo, the comparison is based on whether
     *    the tasks are marked as done. Tasks that are not done come before tasks that
     *    are done. If both tasks have the same "done" status, their names are compared
     *    lexicographically.
     * 2. If the other task is not an instance of ToDo, this task is considered smaller
     *    (ToDo < Deadline < Event < everything else).
     *
     * @param t the task to be compared with this task.
     * @return a negative integer, zero, or a positive integer as this task is less than,
     *         equal to, or greater than the specified task.
     * @throws NullPointerException if the specified task is null.
     */
    public int compareTo(Task t) {
        if (t instanceof ToDo) {
            if (this.isDone != t.isDone) {
                return this.isDone ? 1 : -1;
            } else {
                return this.name.compareTo(t.getName());
            }
        } else { // todo < deadline < event < everything else
            return -1;
        }
    }
}
