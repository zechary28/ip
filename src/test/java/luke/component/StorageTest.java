package luke.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class StorageTest {
    private static final String READ_FILE_PATH = "data/list.txt";
    private static final String WRITE_FILE_PATH = "data/list.txt";
    private Scanner scanner;

    public StorageTest() throws FileNotFoundException {
        this.scanner = new Scanner(new File(READ_FILE_PATH));
    }

    public String readLine() {
        return this.scanner.nextLine();
    }

    public void writeLine(String textToAdd) throws IOException {
        FileWriter writer = new FileWriter(WRITE_FILE_PATH, true);
        writer.write(textToAdd + "\n");
        writer.close();
    }

    public boolean hasNext() { return scanner.hasNext(); }

    public boolean hasReadFile() {
        try {
            new FileWriter(READ_FILE_PATH, true).close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean hasWriteFile() {
        try {
            new FileWriter(WRITE_FILE_PATH, true).close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void clearFile() throws IOException {
        new FileWriter(WRITE_FILE_PATH, false).close();
    }
}
