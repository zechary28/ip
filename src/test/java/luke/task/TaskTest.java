package luke.task;

// abstract Task class
public abstract class TaskTest {
    protected String name;
    protected boolean isDone;

    public TaskTest(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    public abstract String getName();

    public abstract void setIsDone(boolean isDone);

    public abstract boolean getIsDone();

    @Override
    public abstract String toString();
}
