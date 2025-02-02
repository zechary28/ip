import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// prev working commit

public class Luke {

    public static ArrayList<Task> list;
    public static int numItems;
    public static final String LIST_FILE_PATH = "../../../data/list.txt";
    private TaskList tasklist;

    private TaskList taskList;
    private Ui ui;
    private Storage storage;

    public Luke() {
        this.tasklist = new TaskList();
        this.ui = new Ui();
        this.storage = new Storage();
    }

    // abstract Task class
    public static abstract class Task {
        protected String name;
        protected boolean isDone;

        public Task(String name, boolean isDone) {
            this.name = name;
            this.isDone = isDone;
        }

        public abstract void setIsDone(boolean isDone);

        @Override
        public abstract String toString();
    }

    // Task subtypes
    public static class ToDo extends Task {

        protected String dueDate;

        public ToDo(String name, boolean isDone) {
            super(name, isDone);
        }

        public void setIsDone(boolean isDone) {
            this.isDone = isDone;
        }

        @Override
        public String toString() {
            return String.format("[T][%s] %s", this.isDone?"X":" ", this.name);
        }
    }

    public static class Deadline extends Task {

        protected LocalDateTime dueTime;

        public Deadline(String name, boolean isDone, String dueTime) {
            super(name, isDone);
            // input format of dueTime: [DD/MM/YYYY HH:MM]
            // required format        : [YYYY:MM:DDTHH:MM:SS]
            String day = dueTime.substring(0, 2), month = dueTime.substring(3, 5), year = dueTime.substring(6, 10);
            String hour = dueTime.substring(11, 13), minute = dueTime.substring(14, 16);
            String timeString = String.format("%s-%s-%sT%s:%s:00", year, month, day, hour, minute);
            System.out.println(timeString);
            this.dueTime = LocalDateTime.parse(timeString); // assume string is in correct format
        }

        public void setIsDone(boolean isDone) {
            this.isDone = isDone;
        }

        @Override
        public String toString() {
            return String.format("[D][%s] %s (by: %s)", this.isDone?"X":" "
                    , this.name, this.dueTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        }
    }

    public static class Event extends Task {

        protected LocalDateTime startTime;
        protected LocalDateTime endTime;

        public Event(String name, boolean isDone, String start, String end) {
            super(name, isDone);
            // input format of dueTime: [DD/MM/YYYY HH:MM]
            // required format        : [YYYY:MM:DDTHH:MM:SS]
            String startDay = start.substring(0, 2), startMonth = start.substring(3, 5), startYear = start.substring(6, 10);
            String startHour = start.substring(11, 13), startMinute = start.substring(14, 16);
            String startTimeString = String.format("%s-%s-%sT%s:%s:00", startYear, startMonth, startDay, startHour, startMinute);
            String endDay = end.substring(0, 2), endMonth = end.substring(3, 5), endYear = end.substring(6, 10);
            String endHour = end.substring(11, 13), endMinute = end.substring(14, 16);
            String endTimeString = String.format("%s-%s-%sT%s:%s:00", endYear, endMonth, endDay, endHour, endMinute);
            this.startTime = LocalDateTime.parse(startTimeString);
            this.endTime = LocalDateTime.parse(endTimeString);
        }

        public void setIsDone(boolean isDone) {
            this.isDone = isDone;
        }

        @Override
        public String toString() {
            return String.format("[E][%s] %s (from: %s to: %s)", this.isDone?"X":" ", this.name
                    , this.startTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                    , this.endTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        }
    }

    // exceptions
    public static class InvalidInputException extends Exception {}

    public abstract class Command {
        public abstract void execute(TaskList tl, Ui ui, Storage st);
    }
    // list of commands


    // Luke components
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

    public class Ui {
        //io
        BufferedReader reader;
        PrintWriter writer;

        private static final String LOGO = " _           _\n"
                + "| |    _   _| | _____\n"
                + "| |   | | | | |/ / _ \\\n"
                + "| |___| |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

        public Ui() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            writer = new PrintWriter(System.out);
        }

        // showing to System.out
        public void showLine() {
            System.out.println("____________________________________________________________");
        }

        public void showWelcome() {
            showLine();
            System.out.println("Hello I'm\n" + LOGO);
            showLine();
        }

        public void showError(String message) {
            showLine();
            System.out.println("There was an error: " + message);
            showLine();
        }

        public void exit() {
            showLine();
            System.out.println(" Bye. Hope to see you again soon!");
            showLine();
            System.exit(0);
        }

        // reading from input
        public String readCommand() throws IOException {
            return reader.readLine();
        }
    }

    public class Storage {
        private final String READ_FILE_PATH = "../../../data/list.txt";
        private final String WRITE_FILE_PATH = "../../../data/list.txt";
        Scanner scanner;

        public Storage() {
            Scanner scanner = new Scanner(READ_FILE_PATH);
        }

        public String readLine() {
            return scanner.nextLine();
        }

        public void writeLine(String textToAdd) throws IOException {
            FileWriter writer = new FileWriter(WRITE_FILE_PATH);
            writer.write(textToAdd + "\n");
            writer.close();
        }

        public boolean hasNext() { return scanner.hasNext(); }

        public boolean hasReadFile() {
            try {
                new FileWriter(READ_FILE_PATH, true).close();
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        public boolean hasWriteFile() {
            try {
                new FileWriter(WRITE_FILE_PATH, true).close();
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        public void clearFile() throws IOException {
            new FileWriter(WRITE_FILE_PATH, false).close();
        }
    }

    public static void main(String[] args) throws IOException, InvalidInputException, NumberFormatException, FileNotFoundException {
        //io
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        //data structures

        String logo = " _           _\n"
                + "| |    _   _| | _____\n"
                + "| |   | | | | |/ / _ \\\n"
                + "| |___| |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        printLine();
        System.out.println("Hello I'm\n" + logo);
        printLine();
        boolean foundList = true;
        System.out.println("Checking for saved list...");
        try {
            readListFile(); // will build the list
        } catch (FileNotFoundException e) {
            System.out.println("No list found");
            System.out.println("Creating new list...");
            numItems = 0;
            foundList = false;
        } finally {

            // print out existing list
            if (foundList) {
                System.out.println("Here is your current list:");
                printList();
                printLine();
            }

            // main loop
            while (true) {
                try {
                    String input = reader.readLine();
                    if (input == null || input.trim().isEmpty()) {
                        throw new InvalidInputException();
                    }
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
//                            handleToDo(input);
                        } else if (type.equals("deadline")) {
//                            handleDeadline(input);
                        } else if (type.equals("event")) {
//                            handleEvent(input);
                        } else {
                            throw new InvalidInputException();
                        }
                        addTaskUpdates();
                    }
                } catch (InvalidInputException e) {
                    printLine();
                    System.out.println(" OOPS!!! I'm sorry, but I don't know what that means or the input is invalid.");
                    printLine();
                } catch (NumberFormatException e) {
                    printLine();
                    System.out.println(" OOPS!!! Invalid number format. Please enter a valid index.");
                    printLine();
                } catch (ArrayIndexOutOfBoundsException e) {
                    printLine();
                    System.out.println(" OOPS!!! Input is missing required arguments.");
                    printLine();
                } catch (Exception e) {
                    printLine();
                    System.out.println(" OOPS!!! An unexpected error occurred: " + e.getMessage());
                    printLine();
                }
            }
            writeListToFile();
            exit();
        }
    }

    public void run() {
        this.ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
//                Lukebackup.Command c = Parser.parse(fullCommand);
//                c.execute(tasks, ui, storage);
//                isExit = c.isExit();
            } catch (Exception e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
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

    public void readListFile() throws FileNotFoundException {
        if (this.storage.hasReadFile()) {
            String header = this.storage.readLine();
            while (this.storage.hasNext()) {
                String task = this.storage.readLine();
                try {
                    readTask(task);
                } catch (InvalidInputException e) {
                    this.ui.showLine();
                    System.out.println(" There was something wrong with this task.");
                    System.out.println(" " + task);
                    this.ui.showLine();
                }
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
        for (Task task : tasklist.getList()) {
            if (task instanceof ToDo) {
                ToDo todo = (ToDo) task;
                this.storage.writeLine(String.format("T : %s : %s", todo.isDone? "1" : "0", todo.name));
            } else if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                this.storage.writeLine(String.format("D : %s : %s : %s", deadline.isDone? "1" : "0", deadline.name
                        , deadline.dueTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            } else if (task instanceof Event) {
                Event event = (Event) task;
                this.storage.writeLine(String.format("E : %s : %s : %s : %s", event.isDone? "1" : "0", event.name
                        , event.startTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        , event.endTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            }
        }
        storage.writeLine(String.format("list: %d", this.tasklist.getSize()));
        System.out.println("Saved successfully");
    }
}
