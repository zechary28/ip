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
    public class InvalidInputException extends Exception {}

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
            String input = reader.readLine();
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
                }
                addTaskUpdates();
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

    public static void handleToDo(String input) {
        list.add(new ToDo(input.substring(5)));
    }

    public static void handleDeadline(String input) {
        input = input.substring(9); // trip type
        String[] inputArr = input.split(" /by ");
        String name = inputArr[0];
        String due = inputArr[1];
        list.add(new Deadline(name, due));
    }

    public static void handleEvent(String input) {
        input = input.substring(6); // extract type
        String[] inputArr = input.split(" /from "); input = inputArr[1];
        String name = inputArr[0]; // extract name
        inputArr = input.split(" /to ");
        String start = inputArr[0];
        String end = inputArr[1];
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
