package luke.task;

/**
 * The {@code Task} class represents an abstract task that has a name and a completion status.
 * Subclasses must implement the methods to get and set the task's name, completion status,
 * and provide a string representation of the task.
 */
public abstract class Task {

    protected String name;
    protected boolean isDone;

    /**
     * Constructs a new {@code Task} with the specified name and completion status.
     *
     * @param name the name of the task
     * @param isDone the completion status of the task
     */
    public Task(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    /**
     * Gets the name of the task.
     *
     * @return the name of the task
     */
    public abstract String getName();

    /**
     * Sets the completion status of the task.
     *
     * @param isDone {@code true} if the task is done, {@code false} otherwise
     */
    public abstract void setIsDone(boolean isDone);

    /**
     * Gets the completion status of the task.
     *
     * @return {@code true} if the task is done, {@code false} otherwise
     */
    public abstract boolean getIsDone();

    /**
     * Returns a string representation of the task.
     *
     * @return a string representation of the task
     */
    @Override
    public abstract String toString();
}
