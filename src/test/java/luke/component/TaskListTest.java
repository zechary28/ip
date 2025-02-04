package luke.component;

import luke.task.TaskTest;
import luke.task.ToDoStub;
import luke.task.DeadlineStub;
import luke.task.EventStub;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


public class TaskListTest {
    public ArrayList<TaskTest> list;

    public TaskListTest() {
        this.list = new ArrayList<>();
    }

    @Test
    public void addTask_addToDo_containsToDo() {
        TaskListTest tl = new TaskListTest();
        ToDoStub td = new ToDoStub("", true);
        tl.addTask(td);
        assertEquals(td, tl.list.get(0));
    }

    @Test
    public void getTask_containsEventAtIndex1_GetEvent_returnsEvent() {
        TaskListTest tl = new TaskListTest();
        EventStub ev = new EventStub("",true,"","");
        tl.addTask(new ToDoStub("", true));
        tl.addTask(ev);
        assertEquals(ev, tl.list.get(1));
    }

    @Test
    public void getSize_add_return() {
        TaskListTest tl = new TaskListTest();
        tl.addTask(new EventStub("", true, "", ""));
        tl.addTask(new DeadlineStub("", true, ""));
        assertEquals(2, tl.getSize());
    }

    @Test
    public void deleteTask_2TasksDelNumber2_returnLastTask() {
        TaskListTest tl = new TaskListTest();
        tl.addTask(new EventStub("", true, "", ""));
        DeadlineStub dl = new DeadlineStub("", true, "");
        tl.addTask(dl);
        assertEquals(dl, tl.deleteTask(1)); // 0-indexed
    }

    @Test
    public void getList_2TasksInList_returnCorrectList() {
        TaskListTest tl = new TaskListTest();
        ArrayList<TaskTest> list = new ArrayList<>();
        ToDoStub td = new ToDoStub("", true);
        tl.addTask(td);
        list.add(td);
        EventStub ev = new EventStub("",true,"","");
        tl.addTask(ev);
        list.add(ev);
        assertEquals(list, tl.getList());
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
