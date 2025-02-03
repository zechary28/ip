package duke.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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
