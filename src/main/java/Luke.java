import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Luke {

    public static ArrayList<Task> list;
    public static int numItems;

    // abstract class
    public static abstract class Task {
        protected String name;
        protected boolean isDone;

        public Task(String name) {
            this.name = name;
            this.isDone = false;
        }

        public abstract void setIsDone(boolean isDone);

        @Override
        public abstract String toString();
    }

    // Task subtypes
    public static class ToDo extends Task {

        protected String dueDate;

        public ToDo(String name) {
            super(name);
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

        protected String dueTime;

        public Deadline(String name, String dueTime) {
            super(name);
            this.dueTime = dueTime;
        }

        public void setIsDone(boolean isDone) {
            this.isDone = isDone;
        }

        @Override
        public String toString() {
            return String.format("[D][%s] %s (by: %s)", this.isDone?"X":" ", this.name, this.dueTime);
        }
    }

    public static class Event extends Task {

        protected String startTime;
        protected String endTime;

        public Event(String name, String start, String end) {
            super(name);
            this.startTime = start;
            this.endTime = end;
        }

        public void setIsDone(boolean isDone) {
            this.isDone = isDone;
        }

        @Override
        public String toString() {
            return String.format("[E][%s] %s (from: %s to: %s)", this.isDone?"X":" ", this.name, this.startTime, this.endTime);
        }
    }

    // exceptions
    public static class InvalidInputException extends Exception {}

    public static void main(String[] args) throws IOException, InvalidInputException {
        //io
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        //data structures
        list = new ArrayList<>();

        String logo = " _           _\n"
                + "| |    _   _| | _____\n"
                + "| |   | | | | |/ / _ \\\n"
                + "| |___| |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        printLine();
        System.out.println("Hello I'm\n" + logo);
        System.out.println("What can I do for you?");

        numItems = 0;
        while (true) {
            try {
                String input = reader.readLine();
                if (input == null || input.trim().isEmpty()) {
                    throw new InvalidInputException();
                }
                String[] inputArr = input.split(" ");
                if (inputArr[0].equals("bye")) exit();
                else if (inputArr[0].equals("mark")) markTask(Integer.parseInt(inputArr[1]), true);
                else if (inputArr[0].equals("unmark")) markTask(Integer.parseInt(inputArr[1]), false);
                else if (inputArr[0].equals("list")) printList();
                else { // add tasks
                    String type = inputArr[0];
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
    }

    public static void printLine() {

        System.out.println("____________________________________________________________");
    }

    public static void exit() {
        printLine();
        System.out.println(" Bye. Hope to see you again soon!");
        printLine();
        System.exit(0);
    }

    public static void handleToDo(String input) throws InvalidInputException {
        // invalid input: [todo] or [todo ]
        if (input.length() < 5 || input.substring(5).trim().isEmpty()) {
            System.out.println("invalid input: [todo] or [todo ]");
            throw new InvalidInputException();
        }
        String name = input.substring(5);
        list.add(new ToDo(name));
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
        System.out.println(inputArr[0]);
        System.out.println(inputArr[1]);
        String name = inputArr[0];
        String due = inputArr[1];
        // invalid input: white spaces for name and deadline
        if (name.trim().isEmpty() || due.trim().isEmpty()) {
            System.out.println("invalid input: empty task name or deadline");
            throw new InvalidInputException();
        }
        list.add(new Deadline(name, due));
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
        list.add(new Event(name, start, end));
    }

    public static void addTaskUpdates() {
        printLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + list.get(numItems));
        numItems++;
        System.out.println(" Now you have " + numItems + " tasks in the list.");
        printLine();
    }

    public static void printList() {
        printLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < numItems; i++) {
            System.out.println(String.format(" %d.%s",i+1, list.get(i)));
        }
        printLine();
    }

    public static void markTask(int i, boolean isDone) {
        Task task = list.get(i-1);
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

    public static void deleteTask(int i) {
        Task task = list.remove(i);
        printLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("    " + task);
        numItems--;
        System.out.println(" Now you have " + numItems + " tasks in the list.");
        printLine();
    }

}
