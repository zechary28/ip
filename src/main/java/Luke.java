import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Luke {
    public static void main(String[] args) throws IOException {
        //io
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        String logo = " _           _        \n"
                + "| |    _   _| | _____ \n"
                + "| |   | | | | |/ / _ \\\n"
                + "| |___| |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("_____________________________________");
        System.out.println("Hello I'm\n" + logo);
        System.out.println("What can I do for you?");

        while (true) {
            String input = reader.readLine();
            if (input.equals("bye")) exit();
            else System.out.println(input);
        }
    }

    public static void printLine() {
        System.out.println("_____________________________________");
    }

    public static void exit() {
        printLine();
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
    }
}
