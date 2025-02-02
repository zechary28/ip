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

public class Lukebackup {

    private TaskList tasklist;
    private Ui ui;
    private Storage storage;

    public Luke() {
        this.tasklist = new TaskList();
        this.ui = new Ui();
    }

    // abstract class
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

    public abstract class Command {
        public abstract void execute(TaskList tl, Ui ui, Storage st);
    }

    public class WriteListToFileCommand extends Command {
        public WriteListToFileCommand() {

        }
        public void execute(TaskList tl, Ui ui, Storage st) {
            try {
                System.out.println("Searching for list file...");
                new FileWriter(this.WRITE_FILE_PATH, false).close(); // clear file
            } catch (IOException e) {
                System.out.println("Could not find file " + e.getMessage());
                System.out.println("Creating new file...");
            } finally {
                System.out.println("Saving list...");
                FileWriter writer = new FileWriter(LIST_FILE_PATH, true);
                writer.write(String.format("list: %d\n", tasklist.getSize()));
                for (Task task : list) {
                    if (task instanceof ToDo) {
                        ToDo todo = (ToDo) task;
                        writer.write(String.format("T : %s : %s", todo.isDone? "1" : "0", todo.name));
                    } else if (task instanceof Deadline) {
                        Deadline deadline = (Deadline) task;
                        writer.write(String.format("D : %s : %s : %s", deadline.isDone? "1" : "0", deadline.name
                                , deadline.dueTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
                    } else if (task instanceof Event) {
                        Event event = (Event) task;
                        writer.write(String.format("E : %s : %s : %s : %s", event.isDone? "1" : "0", event.name
                                , event.startTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                                , event.endTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
                    }
                    writer.write("\n");
                }
                writer.flush();
                writer.close();
                System.out.println("Saved successfully");
            }
        }
    }

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

        public void deleteTask(int i) {
            Task task = this.list.remove(i);
            printLine();
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + task);
            System.out.println(" Now you have " + list.size() + " tasks in the list.");
            printLine();
        }

        public Task getTask(int i) {
            return this.list.get(i);
        }

        public void markTask(int i, boolean isDone) {
            Task task = this.list.get(i);
            task.setIsDone(isDone);
            printLine();
            if (isDone) {
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + task.toString());
                printLine();
            } else {
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + task.toString());
                printLine();
            }
        }

        public void showList() {
            printLine();
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < this.list.size(); i++) {
                System.out.println(String.format(" %d.%s",i+1, this.list.get(i)));
            }
            printLine();
        }
    }

    public class Ui {
        //io
        BufferedReader reader;
        PrintWriter writer;

        String logo = " _           _\n"
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
            System.out.println("Hello I'm\n" + logo);
            showLine();
        }

        public void showTaskUpdates(Task task, int num) {
            this.showLine();
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + task);
            System.out.println(" Now you have " + num + " tasks in the list.");
            this.showLine();
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

        public String writeLine() {

        }

        public ArrayList<Task> readListFile() throws FileNotFoundException {
            File file = new File(this.READ_FILE_PATH);
            String header = scanner.nextLine();
            System.out.println(header);
            int num = Integer.parseInt(header.substring(6));
            while (scanner.hasNext()) {
                String task = scanner.nextLine();
                try {
                    readTask(task);
                } catch (InvalidInputException e) {
                    System.out.println(" There was something wrong with this task.");
                    System.out.println(" " + task);
                }
            }
        }

        public static Task readTask(String input) throws InvalidInputException {
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

        public void writeListToFile() throws IOException, FileNotFoundException {

        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError("There was an error with the input");
            } finally {
                ui.showLine();
            }
        }


        //data structures

        boolean foundList = true;
        System.out.println("Checking for saved list...");
        try {
            readListFile(); // will build the list
        } catch (FileNotFoundException e) {
            System.out.println("No list found");
            System.out.println("Creating new list...");
            foundList = false;
        } finally {

            // print out existing list
            if (foundList) {
                System.out.println("Here is your current list:");
                tasklist.printList();
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
                    else if (command.equals("list")) this.tasklist.printList();
                    else if (command.equals("delete")) this.tasklist.deleteTask(Integer.parseInt(inputArr[1]) - 1);
                    else { // add tasks
                        String type = command;
                        if (type.equals("todo")) {
                            handleToDo(input);
                        } else if (type.equals("deadline")) {
                            handleDeadline(input);
                        } else if (type.equals("event")) {
                            handleEvent(input);
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
            try {
                writeListToFile();
            } catch (Exception e) {

            }
            exit();
        }
    }

    // exceptions
    public static class InvalidInputException extends Exception {}

    public static void main(String[] args) throws IOException, InvalidInputException, NumberFormatException, FileNotFoundException {

        new Luke().run();

        public static void handleToDo(String input) throws InvalidInputException {
            // invalid input: [todo] or [todo ]
            if (input.length() < 5 || input.substring(5).trim().isEmpty()) {
                System.out.println("invalid input: [todo] or [todo ]");
                throw new InvalidInputException();
            }
            String name = input.substring(5);
            tasklist.add(new ToDo(name, false));
        }

        public static void handleDeadline(String input) throws InvalidInputException {
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
            tasklist.add(new Deadline(name, false, due));
        }

        public static void handleEvent(String input) throws InvalidInputException {
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
            tasklist.add(new Event(name, false, start, end));
        }
    }
}
