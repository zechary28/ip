package luke.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The {@code Storage} class is responsible for reading from a file located at {@link #READ_FILE_PATH}
 * and writing to a file located at {@link #WRITE_FILE_PATH}.
 * It provides methods to read lines from the file, write lines to the file,
 * check if the file exists and has content, and clear the file.
 */
public class Storage {
    private static final String READ_FILE_PATH = "src/main/resources/data/list.txt";
    private static final String WRITE_FILE_PATH = "src/main/resources/data/list.txt";
    private static final String JAR_READ_FILE_PATH = "data/list.txt";
    private static final String JAR_WRITE_FILE_PATH = "data/list.txt";
    private Scanner scanner;
    private boolean isJar = false;

    /**
     * Constructs a Storage object and initializes a Scanner to read from the file
     * located at "data/list.txt".
     *
     * @throws FileNotFoundException if the file at the READ_FILE_PATH does not exist
     */
    public Storage() {
        try {
            this.scanner = new Scanner(new File(READ_FILE_PATH));
        } catch (FileNotFoundException e) {
            isJar = true;
            try {
                this.scanner = new Scanner(new File(JAR_READ_FILE_PATH));
            } catch (FileNotFoundException e2) {
                System.out.println("No file found");
            }
        }
    }

    /**
     * Reads the next line from the file.
     *
     * @return the next line of text in the file
     */
    public String readLine() {
        try {
            return this.scanner.nextLine();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Writes the specified text to the file located at WRITE_FILE_PATH.
     * A new line is added after the text.
     *
     * @param textToAdd the text to be written to the file
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public void writeLine(String textToAdd) throws IOException {
        String writeFilePath = isJar ? JAR_WRITE_FILE_PATH : WRITE_FILE_PATH;

        // Create directory if it doesn't exist
        File directory = new File(writeFilePath).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs(); // Create directories
            System.out.println("Directory created: ");
        }

        // Create the file if it doesn't exist
        File file = new File(writeFilePath);
        if (!file.exists()) {
            file.createNewFile(); // Create file
            System.out.println("File created: " + writeFilePath);
        }

        // Use try-with-resources to ensure the writer is closed properly
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(textToAdd + System.lineSeparator());
        }
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
        String readFilePath = isJar ? JAR_READ_FILE_PATH : READ_FILE_PATH;
        try {
            new FileWriter(readFilePath, true).close();
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
        String writeFilePath = isJar ? JAR_WRITE_FILE_PATH : WRITE_FILE_PATH;
        try {
            new FileWriter(writeFilePath, true).close();
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
        String writeFilePath = isJar ? JAR_WRITE_FILE_PATH : WRITE_FILE_PATH;
        new FileWriter(writeFilePath, false).close();
    }

    public File createEmptyFile() throws IOException {
        File file = new File(JAR_READ_FILE_PATH);
        file.getParentFile().mkdirs(); // Create parent directories if they don't exist
        return file;
    }
}
