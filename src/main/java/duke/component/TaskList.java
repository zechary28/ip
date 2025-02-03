package duke.component;

import duke.task.Task;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> list;

    public TaskList() {
        this.list = new ArrayList<>();
    }

    public int getSize() {
        return list.size();
    }

    public void addTask(Task task) {
        this.list.add(task);
    }

    public Task deleteTask(int i) {
        return this.list.remove(i);
    }

    public Task getTask(int i) {
        return this.list.get(i);
    }

    public void markTask(int i, boolean isDone) {
        this.list.get(i).setIsDone(isDone);
    }

    public ArrayList<Task> getList() {
        return this.list;
    }
}
