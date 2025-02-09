package luke.component;

import java.util.ArrayList;

import luke.task.Task;

/**
 * The {@code TaskList} class represents a list of tasks.
 * It provides methods to add, delete, retrieve, and modify tasks in the list.
 * Tasks are represented by {@link Task} objects.
 */
public class TaskList {

    private ArrayList<Task> list;

    /**
     * Constructs a new {@code TaskList} object and initializes an empty list of tasks.
     */
    public TaskList() {
        this.list = new ArrayList<>();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the number of tasks in the list
     */
    public int getSize() {
        return list.size();
    }

    /**
     * Adds a task to the list.
     *
     * @param task the task to be added
     */
    public void addTask(Task task) {
        this.list.add(task);
    }

    /**
     * Deletes a task from the list by its index.
     *
     * @param i the index of the task to be deleted
     * @return the task that was removed
     */
    public Task deleteTask(int i) {
        return this.list.remove(i);
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param i the index of the task to be retrieved
     * @return the task at the specified index
     */
    public Task getTask(int i) {
        return this.list.get(i);
    }

    /**
     * Marks a task as done or not done based on the specified flag.
     *
     * @param i the index of the task to be marked
     * @param isDone {@code true} to mark the task as done, {@code false} to mark it as not done
     */
    public void markTask(int i, boolean isDone) {
        this.list.get(i).setIsDone(isDone);
    }

    /**
     * Returns the list of tasks.
     *
     * @return the list of tasks
     */
    public ArrayList<Task> getList() {
        return this.list;
    }
}
