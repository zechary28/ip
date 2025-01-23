import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Luke {

    public static Task[] list;
    public static int numItems;

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

    public class Deadline extends Task {

        protected String dueDate;

        public Deadline(String name, String dueDate) {
            super(name);
            this.dueDate = dueDate;
        }

        public void setIsDone(boolean isDone) {
            this.isDone = isDone;
        }

        @Override
        public String toString() {
            return String.format("[D][%s] %s (by: %s)", this.isDone?"X":" ", this.name);
        }
    }

    public class ToDo extends Task {

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

    public static void main(String[] args) throws IOException {
        //io
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        //data structures
        list = new Task[100];

        String logo = " _           _        \n"
                + "| |    _   _| | _____ \n"
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
            else {
//                list[numItems] = new Task(input);
                numItems++;
                printLine();
                System.out.println("added: " + input);
                printLine();
            }
        }
    }

    public static void printLine() {
        System.out.println("_____________________________________");
    }

    public static void exit() {
        printLine();
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
        System.exit(0);
    }

    public static void printList() {
        for (int i = 0; i < numItems; i++) {
            System.out.println(String.format("%d. %s",i+1, list[i]));
        }
    }

    public static void markTask(int i, boolean isDone) {
        Task task = list[i-1];
        task.setIsDone(isDone);
        printLine();
        if (isDone) System.out.println("Nice! I've marked this task as done:");
        else System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task.toString());
        printLine();
    }
}
