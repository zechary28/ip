package luke.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import luke.component.StorageTest;
import luke.component.TaskListTest;
import luke.component.UiTest;
import luke.exception.InvalidInputExceptionTest;
import luke.task.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LukeTest {

    private TaskListTest tasklist;
    private UiTest uiTest;
    private StorageTest storageTest;

    public LukeTest() {
        this.tasklist = new TaskListTest();
        this.uiTest = new UiTest();
        try {
            this.storageTest = new StorageTest();
        } catch (Exception e) {
            System.out.println("No file for storage found, exiting program");
            System.exit(0);
        }
    }

    public static void main(String[] args) throws IOException, InvalidInputExceptionTest, NumberFormatException, FileNotFoundException {
        new LukeTest().run();
    }

    public void run() {
        this.uiTest.showWelcome();
        checkListFile();
        while (true) {
            try {
                String input = uiTest.readCommand();
                if (input == null || input.trim().isEmpty()) {
                    System.out.println("No input detected");
                    throw new InvalidInputExceptionTest();
                }
                // determine command
                String[] inputArr = input.split(" ");
                String command = inputArr[0];
                if (command.equals("bye")) break;
                else if (command.equals("mark")) markTask(Integer.parseInt(inputArr[1]) - 1, true);
                else if (command.equals("unmark")) markTask(Integer.parseInt(inputArr[1]) - 1, false);
                else if (command.equals("list")) printList();
                else if (command.equals("delete")) deleteTask(Integer.parseInt(inputArr[1]) - 1);
                else { // add tasks
                    String type = command;
                    if (type.equals("todo")) {
                        TaskTest taskTest = parseToDo(input);
                        this.tasklist.addTask(taskTest);
                        addTaskUpdates(taskTest);
                    } else if (type.equals("deadline")) {
                        TaskTest taskTest = parseDeadline(input);
                        this.tasklist.addTask(taskTest);
                        addTaskUpdates(taskTest);
                    } else if (type.equals("event")) {
                        TaskTest taskTest = parseEvent(input);
                        this.tasklist.addTask(taskTest);
                        addTaskUpdates(taskTest);
                    } else {
                        throw new InvalidInputExceptionTest();
                    }
                }
            } catch (Exception e) {
                uiTest.showError(e.getMessage());
            } finally {
                uiTest.showLine();
            }
        }
        try {
            writeListToFile();
        } catch (Exception e) {

        }
        this.uiTest.exit();
    }

    // parsers
    public static TaskTest parseToDo(String input) throws InvalidInputExceptionTest {
        // invalid input: [t0do] or [t0do ]
        if (input.length() < 5 || input.substring(5).trim().isEmpty()) {
            System.out.println("invalid input: [todo] or [todo ]");
            throw new InvalidInputExceptionTest();
        }
        String name = input.substring(5);
        return new ToDoTest(name, false);
    }

    public static TaskTest parseDeadline(String input) throws InvalidInputExceptionTest {
        // invalid input: [deadline] or [deadline ]
        if (input.length() < 9 || input.substring(9).trim().isEmpty()) {
            System.out.println("invalid input: [deadline] or [deadline ]");
            throw new InvalidInputExceptionTest();
        }
        String[] inputArr = input.substring(9).split(" /by ");
        // invalid input: [deadline *** /by ] or [deadline /by ***]
        if (inputArr.length < 2) {
            System.out.println(inputArr[0]);
            System.out.println("invalid input: [deadline *** /by ] or [deadline /by ***]");
            throw new InvalidInputExceptionTest();
        }
        String name = inputArr[0];
        String due = inputArr[1];
        // invalid input: white spaces for name and deadline
        if (name.trim().isEmpty() || due.trim().isEmpty()) {
            System.out.println("invalid input: empty task name or deadline");
            throw new InvalidInputExceptionTest();
        }
        return new DeadlineTest(name, false, due);
    }

    public static TaskTest parseEvent(String input) throws InvalidInputExceptionTest {
        // invalid input: [event] or [event ]
        if (input.length() < 6 || input.substring(6).trim().isEmpty()) {
            System.out.println("invalid input: [event] or [event ]");
            throw new InvalidInputExceptionTest();
        }

        // split [name] /from [rest...]
        String[] inputArr = input.substring(6).split(" /from ");
        // invalid input: [event *** /from ] or [event /from ***]
        if (inputArr.length < 2) {
            System.out.println("invalid input: [event *** /from ] or [event /from ***]");
            throw new InvalidInputExceptionTest();
        }
        input = inputArr[1];
        String name = inputArr[0]; // extract name

        // split [start] /by [end]
        inputArr = input.split(" /to ");
        // invalid input: [event *** /from *** /to ] or [event *** /from /to ***]
        if (inputArr.length < 2) {
            System.out.println("invalid input: [event *** /from *** /to ] or [event *** /from /to ***]");
            throw new InvalidInputExceptionTest();
        }
        String start = inputArr[0];
        String end = inputArr[1];
        if (start.trim().isEmpty() || end.trim().isEmpty()) throw new InvalidInputExceptionTest();
        return new EventTest(name, false, start, end);
    }

    public void addTaskUpdates(TaskTest taskTest) {
        this.uiTest.showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + taskTest);
        System.out.println(" Now you have " + tasklist.getSize() + " tasks in the list.");
        this.uiTest.showLine();
    }

    public void printList() {
        this.uiTest.showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < this.tasklist.getSize(); i++) {
            System.out.println(String.format(" %d.%s",i+1, this.tasklist.getList().get(i)));
        }
        this.uiTest.showLine();
    }

    public void markTask(int i, boolean isDone) {
        TaskTest taskTest = this.tasklist.getTask(i);
        taskTest.setIsDone(isDone);
        this.uiTest.showLine();
        if (isDone) {
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + taskTest);
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + taskTest);
        }
        this.uiTest.showLine();
    }

    public void deleteTask(int i) {
        TaskTest taskTest = this.tasklist.deleteTask(i);
        this.uiTest.showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + taskTest);
        System.out.println(" Now you have " + this.tasklist.getSize() + " tasks in the list.");
        this.uiTest.showLine();
    }

    public void checkListFile() {
        boolean foundList = true;
        try {
            readListFile();
        } catch (FileNotFoundException e) {
            System.out.println("No list found");
            System.out.println("Creating new list...");
            foundList = false;
        } finally {
            if (foundList) {
                printList();
                this.uiTest.showLine();
            }
        }
    }

    public void readListFile() throws FileNotFoundException {
        String header = this.storageTest.readLine();
        while (this.storageTest.hasNext()) {
            String task = this.storageTest.readLine();
            try {
                this.tasklist.addTask(readTask(task));
            } catch (InvalidInputExceptionTest e) {
                this.uiTest.showLine();
                System.out.println(" There was something wrong with this task.");
                System.out.println(" " + task);
                this.uiTest.showLine();
            }
        }
    }

    public TaskTest readTask(String input) throws InvalidInputExceptionTest {
        String[] task = input.split(" : ");
        String taskType = task[0];
        if (taskType.equals("T")) {
            if (task.length < 3) {
                System.out.println("len < 3");
                throw new InvalidInputExceptionTest();
            }
            boolean isDone = task[1].equals("1");
            String name = task[2];
            return new ToDoTest(name, isDone);
        } else if (taskType.equals("D")) {
            if (task.length < 4) {
                System.out.println("len < 4");
                throw new InvalidInputExceptionTest();
            }
            boolean isDone = task[1].equals("1");
            String name = task[2];
            String deadline = task[3];
            return new DeadlineTest(name, isDone, deadline);
        } else if (taskType.equals("E")) {
            if (task.length < 5) {
                System.out.println("len < 5");
                throw new InvalidInputExceptionTest();
            }
            boolean isDone = task[1].equals("1");
            String name = task[2];
            String start = task[3];
            String end = task[4];
            return new EventTest(name, isDone, start, end);
        } else {
            System.out.println("invalid command");
            throw new InvalidInputExceptionTest();
        }
    }

    public void writeListToFile() throws IOException {
        this.storageTest.clearFile();
        System.out.println("Saving list...");
        storageTest.writeLine(String.format("list: %d", this.tasklist.getSize()));
        for (TaskTest taskTest : tasklist.getList()) {
            if (taskTest instanceof ToDoTest) {
                ToDoTest todo = (ToDoTest) taskTest;
                this.storageTest.writeLine(String.format("T : %s : %s", todo.getIsDone()? "1" : "0", todo.getName()));
            } else if (taskTest instanceof DeadlineTest) {
                DeadlineTest deadlineTest = (DeadlineTest) taskTest;
                this.storageTest.writeLine(String.format("D : %s : %s : %s", deadlineTest.getIsDone()? "1" : "0", deadlineTest.getName()
                        , deadlineTest.getDueTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            } else if (taskTest instanceof EventTest) {
                EventTest eventTest = (EventTest) taskTest;
                this.storageTest.writeLine(String.format("E : %s : %s : %s : %s", eventTest.getIsDone()? "1" : "0", eventTest.getName()
                        , eventTest.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        , eventTest.getEndTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            }
        }
        System.out.println("Saved successfully");
    }
}
