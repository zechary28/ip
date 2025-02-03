package luke.task;

public class ToDo extends Task {

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