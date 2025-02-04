package luke.task;

public class ToDoStub extends TaskTest {

    public ToDoStub(String name, boolean isDone) {
        super(name, isDone);
    }

    public String getName() { return "ToDoStub"; }

    public void setIsDone(boolean isDone) {    }

    public boolean getIsDone() { return true; }

    @Override
    public String toString() {
        return "[T][1] ToDoStub";
    }
}