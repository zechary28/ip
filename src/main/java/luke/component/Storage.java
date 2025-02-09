package luke.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * The {@code Storage} class is responsible for reading from a file located at {@link #READ_FILE_PATH}
 * and writing to a file located at {@link #WRITE_FILE_PATH}.
 * It provides methods to read lines from the file, write lines to the file,
 * check if the file exists and has content, and clear the file.
 */
public class Storage {
    private static final String READ_FILE_PATH = "data/list.txt";
    private static final String WRITE_FILE_PATH = "data/list.txt";
    private Scanner scanner;

    /**
     * Constructs a Storage object and initializes a Scanner to read from the file
     * located at "data/list.txt".
     *
     * @throws FileNotFoundException if the file at the READ_FILE_PATH does not exist
     */
    public Storage() throws FileNotFoundException {
        this.scanner = new Scanner(new File(READ_FILE_PATH));
    }

    /**
     * Reads the next line from the file.
     *
     * @return the next line of text in the file
     */
    public String readLine() {
        return this.scanner.nextLine();
    }

    /**
     * Writes the specified text to the file located at WRITE_FILE_PATH.
     * A new line is added after the text.
     *
     * @param textToAdd the text to be written to the file
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public void writeLine(String textToAdd) throws IOException {
        FileWriter writer = new FileWriter(WRITE_FILE_PATH, true);
        writer.write(textToAdd + "\n");
        writer.close();
    }

    /**
     * Checks if there are more lines to be read from the file.
     *
     * @return true if there are more lines to be read, false otherwise
     */
    public boolean hasNext() {
        return scanner.hasNext();
    }

    /**
     * Checks if the file located at the READ_FILE_PATH exists and can be written to.
     *
     * @return true if the file is accessible for writing, false otherwise
     */
    public boolean hasReadFile() {
        try {
            new FileWriter(READ_FILE_PATH, true).close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the file located at the WRITE_FILE_PATH exists and can be written to.
     *
     * @return true if the file is accessible for writing, false otherwise
     */
    public boolean hasWriteFile() {
        try {
            new FileWriter(WRITE_FILE_PATH, true).close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Clears the content of the file located at WRITE_FILE_PATH.
     * This method overwrites the file with an empty content.
     *
     * @throws IOException if an I/O error occurs while clearing the file
     */
    public void clearFile() throws IOException {
        new FileWriter(WRITE_FILE_PATH, false).close();
    }
}
