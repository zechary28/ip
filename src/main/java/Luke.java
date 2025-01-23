import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Luke {

    public static String[] list;

    public static void main(String[] args) throws IOException {
        //io
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        //data structures
        list = new String[100];

        String logo = " _           _        \n"
                + "| |    _   _| | _____ \n"
                + "| |   | | | | |/ / _ \\\n"
                + "| |___| |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("_____________________________________");
        System.out.println("Hello I'm\n" + logo);
        System.out.println("What can I do for you?");

        int i = 0;
        while (true) {
            String input = reader.readLine();
            if (input.equals("bye")) exit();
            else {
                list[i] = input;
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
        int i = 0;
        for (String task: list) {
            System.out.println(((i+1) + ". " + list[i]));
            i++;
        }
    }
}
