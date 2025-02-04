package luke.component;

import luke.task.TaskTest;

import java.util.ArrayList;

public class TaskListTest {
    private ArrayList<TaskTest> list;

    public TaskListTest() {
        this.list = new ArrayList<>();
    }

    public int getSize() {
        return list.size();
    }

    public void addTask(TaskTest taskTest) {
        this.list.add(taskTest);
    }

    public TaskTest deleteTask(int i) {
        return this.list.remove(i);
    }

    public TaskTest getTask(int i) {
        return this.list.get(i);
    }

    public void markTask(int i, boolean isDone) {
        this.list.get(i).setIsDone(isDone);
    }

    public ArrayList<TaskTest> getList() {
        return this.list;
    }
}
