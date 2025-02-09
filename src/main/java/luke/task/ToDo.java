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
}
