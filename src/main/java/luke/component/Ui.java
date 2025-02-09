package luke.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * The {@code Ui} class provides methods for interacting with the user through
 * the console. It allows displaying messages, error notifications, and reading
 * user input.
 */
public class Ui {

    private static final String LOGO = " _           _\n"
            + "| |    _   _| | _____\n"
            + "| |   | | | | |/ / _ \\\n"
            + "| |___| |_| |   <  __/\n"
            + "|____/ \\__,_|_|\\_\\___|\n";

    // IO
    private BufferedReader reader;
    private PrintWriter writer;

    /**
     * Constructs a new {@code Ui} object, initializing the reader and writer
     * for interacting with the console.
     */
    public Ui() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(System.out);
    }

    /**
     * Displays a separator line in the console.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays a welcome message with the application logo.
     */
    public void showWelcome() {
        showLine();
        System.out.println("Hello I'm\n" + LOGO);
        showLine();
    }

    /**
     * Displays an error message in the console.
     *
     * @param message the error message to be displayed
     */
    public void showError(String message) {
        showLine();
        System.out.println("There was an error: " + message);
        showLine();
    }

    /**
     * Displays an exit message and terminates the program.
     */
    public void exit() {
        showLine();
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
        System.exit(0);
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the line of input entered by the user
     * @throws IOException if an I/O error occurs during input reading
     */
    public String readCommand() throws IOException {
        return reader.readLine();
    }
}
