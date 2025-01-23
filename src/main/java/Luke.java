import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Luke {

    public static Task[] list;
    public static int numItems;

    public static class Task {
        private String name;
        private boolean isDone;

        public Task(String name) {
            this.name = name;
            this.isDone = false;
        }

        public void markDone() {
            this.isDone = true;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s", this.isDone?"X":" ", this.name);
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
        System.out.println("_____________________________________");
        System.out.println("Hello I'm\n" + logo);
        System.out.println("What can I do for you?");

        numItems = 0;
        while (true) {
            String input = reader.readLine();
            if (input.equals("bye")) exit();
            else if (input.equals("list")) printList();
            else {
                list[numItems] = new Task(input);
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
            System.out.println(String.format("%s. %s",i+1, list[i]));
        }
    }
}
