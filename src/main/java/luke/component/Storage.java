package luke.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Storage {
    private final String READ_FILE_PATH = "data/list.txt";
    private final String WRITE_FILE_PATH = "data/list.txt";
    Scanner scanner;

    public Storage() throws FileNotFoundException {
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
