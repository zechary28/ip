package luke.ui;

import luke.task.*;
import luke.component.*;
import luke.exception.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

public class Luke {

    private TaskList tasklist;
    private Ui ui;
    private Storage storage;

    public Luke() {
        this.tasklist = new TaskList();
        this.ui = new Ui();
        try {
            this.storage = new Storage();
        } catch (Exception e) {
            System.out.println("No file for storage found, exiting program");
            System.exit(0);
        }
    }

    public static void main(String[] args) throws IOException, InvalidInputException, NumberFormatException, FileNotFoundException {
        new Luke().run();
    }

    public void run() {
        this.ui.showWelcome();
        checkListFile();
        while (true) {
            try {
                String input = ui.readCommand();
                if (input == null || input.trim().isEmpty()) {
                    System.out.println("No input detected");
                    throw new InvalidInputException();
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
                        Task task = parseToDo(input);
                        this.tasklist.addTask(task);
                        addTaskUpdates(task);
                    } else if (type.equals("deadline")) {
                        Task task = parseDeadline(input);
                        this.tasklist.addTask(task);
                        addTaskUpdates(task);
                    } else if (type.equals("event")) {
                        Task task = parseEvent(input);
                        this.tasklist.addTask(task);
                        addTaskUpdates(task);
                    } else {
                        throw new InvalidInputException();
                    }
                }
            } catch (Exception e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        try {
            writeListToFile();
        } catch (Exception e) {

        }
        this.ui.exit();
    }

    // parsers
    public static Task parseToDo(String input) throws InvalidInputException {
        // invalid input: [t0do] or [t0do ]
        if (input.length() < 5 || input.substring(5).trim().isEmpty()) {
            System.out.println("invalid input: [todo] or [todo ]");
            throw new InvalidInputException();
        }
        String name = input.substring(5);
        return new ToDo(name, false);
    }

    public static Task parseDeadline(String input) throws InvalidInputException {
        // invalid input: [deadline] or [deadline ]
        if (input.length() < 9 || input.substring(9).trim().isEmpty()) {
            System.out.println("invalid input: [deadline] or [deadline ]");
            throw new InvalidInputException();
        }
        String[] inputArr = input.substring(9).split(" /by ");
        // invalid input: [deadline *** /by ] or [deadline /by ***]
        if (inputArr.length < 2) {
            System.out.println(inputArr[0]);
            System.out.println("invalid input: [deadline *** /by ] or [deadline /by ***]");
            throw new InvalidInputException();
        }
        String name = inputArr[0];
        String due = inputArr[1];
        // invalid input: white spaces for name and deadline
        if (name.trim().isEmpty() || due.trim().isEmpty()) {
            System.out.println("invalid input: empty task name or deadline");
            throw new InvalidInputException();
        }
        return new Deadline(name, false, due);
    }

    public static Task parseEvent(String input) throws InvalidInputException {
        // invalid input: [event] or [event ]
        if (input.length() < 6 || input.substring(6).trim().isEmpty()) {
            System.out.println("invalid input: [event] or [event ]");
            throw new InvalidInputException();
        }

        // split [name] /from [rest...]
        String[] inputArr = input.substring(6).split(" /from ");
        // invalid input: [event *** /from ] or [event /from ***]
        if (inputArr.length < 2) {
            System.out.println("invalid input: [event *** /from ] or [event /from ***]");
            throw new InvalidInputException();
        }
        input = inputArr[1];
        String name = inputArr[0]; // extract name

        // split [start] /by [end]
        inputArr = input.split(" /to ");
        // invalid input: [event *** /from *** /to ] or [event *** /from /to ***]
        if (inputArr.length < 2) {
            System.out.println("invalid input: [event *** /from *** /to ] or [event *** /from /to ***]");
            throw new InvalidInputException();
        }
        String start = inputArr[0];
        String end = inputArr[1];
        if (start.trim().isEmpty() || end.trim().isEmpty()) throw new InvalidInputException();
        return new Event(name, false, start, end);
    }

    public void addTaskUpdates(Task task) {
        this.ui.showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + tasklist.getSize() + " tasks in the list.");
        this.ui.showLine();
    }

    public void printList() {
        this.ui.showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < this.tasklist.getSize(); i++) {
            System.out.println(String.format(" %d.%s",i+1, this.tasklist.getList().get(i)));
        }
        this.ui.showLine();
    }

    public void markTask(int i, boolean isDone) {
        Task task = this.tasklist.getTask(i);
        task.setIsDone(isDone);
        this.ui.showLine();
        if (isDone) {
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + task);
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + task);
        }
        this.ui.showLine();
    }

    public void deleteTask(int i) {
        Task task = this.tasklist.deleteTask(i);
        this.ui.showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + this.tasklist.getSize() + " tasks in the list.");
        this.ui.showLine();
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
                this.ui.showLine();
            }
        }
    }

    public void readListFile() throws FileNotFoundException {
        String header = this.storage.readLine();
        while (this.storage.hasNext()) {
            String task = this.storage.readLine();
            try {
                this.tasklist.addTask(readTask(task));
            } catch (InvalidInputException e) {
                this.ui.showLine();
                System.out.println(" There was something wrong with this task.");
                System.out.println(" " + task);
                this.ui.showLine();
            }
        }
    }

    public Task readTask(String input) throws InvalidInputException {
        String[] task = input.split(" : ");
        String taskType = task[0];
        if (taskType.equals("T")) {
            if (task.length < 3) {
                System.out.println("len < 3");
                throw new InvalidInputException();
            }
            boolean isDone = task[1].equals("1");
            String name = task[2];
            return new ToDo(name, isDone);
        } else if (taskType.equals("D")) {
            if (task.length < 4) {
                System.out.println("len < 4");
                throw new InvalidInputException();
            }
            boolean isDone = task[1].equals("1");
            String name = task[2];
            String deadline = task[3];
            return new Deadline(name, isDone, deadline);
        } else if (taskType.equals("E")) {
            if (task.length < 5) {
                System.out.println("len < 5");
                throw new InvalidInputException();
            }
            boolean isDone = task[1].equals("1");
            String name = task[2];
            String start = task[3];
            String end = task[4];
            return new Event(name, isDone, start, end);
        } else {
            System.out.println("invalid command");
            throw new InvalidInputException();
        }
    }

    public void writeListToFile() throws IOException {
        this.storage.clearFile();
        System.out.println("Saving list...");
        storage.writeLine(String.format("list: %d", this.tasklist.getSize()));
        for (Task task : tasklist.getList()) {
            if (task instanceof ToDo) {
                ToDo todo = (ToDo) task;
                this.storage.writeLine(String.format("T : %s : %s", todo.getIsDone()? "1" : "0", todo.getName()));
            } else if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                this.storage.writeLine(String.format("D : %s : %s : %s", deadline.getIsDone()? "1" : "0", deadline.getName()
                        , deadline.getDueTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            } else if (task instanceof Event) {
                Event event = (Event) task;
                this.storage.writeLine(String.format("E : %s : %s : %s : %s", event.getIsDone()? "1" : "0", event.getName()
                        , event.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        , event.getEndTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            }
        }
        System.out.println("Saved successfully");
    }
}
