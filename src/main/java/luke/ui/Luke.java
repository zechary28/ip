package luke.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import luke.component.Storage;
import luke.component.TaskList;
import luke.component.Ui;
import luke.exception.InvalidInputException;
import luke.task.Deadline;
import luke.task.Event;
import luke.task.Task;
import luke.task.ToDo;

/**
 * The {@code Luke} class is the main entry point for the task management application.
 * It handles the interactions with the user, parses commands, and updates the task list.
 * It is responsible for managing tasks, saving/loading task data to/from a file,
 * and providing feedback to the user via the {@link Ui} class.
 */
public class Luke {

    private TaskList taskList;
    private Ui ui;
    private Storage storage;

    private StringBuilder output;

    /**
     * Constructs a new {@code Luke} object which initializes the task list, user interface,
     * and storage system. If the storage system cannot be found, the program will terminate.
     */
    public Luke() {
        this.taskList = new TaskList();
        this.ui = new Ui();
        try {
            this.storage = new Storage();
        } catch (Exception e) {
            System.out.println("No file for storage found, exiting program");
            System.exit(0);
        }
        this.output = new StringBuilder();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        this.output = new StringBuilder();
        if (input == null || input.trim().isEmpty()) {
            return "No input detected";
        }
        // determine command
        String[] inputArr = input.split(" ");
        String command = inputArr[0];
        if (command.equals("bye")) {
            try {
                writeListToFile();
            } catch (Exception e) {
                this.output.append("There was a problem writing to the file\n");
            }
            System.exit(0);
        } else if (command.equals("mark")) {
            markTask(Integer.parseInt(inputArr[1]) - 1, true);
        } else if (command.equals("unmark")) {
            markTask(Integer.parseInt(inputArr[1]) - 1, false);
        } else if (command.equals("list")) {
            printList();
        } else if (command.equals("delete")) {
            deleteTask(Integer.parseInt(inputArr[1]) - 1);
        } else if (command.equals("find")) {
            findTask(inputArr[1]);
        } else { // add tasks
            try {
                String taskType = command;
                if (taskType.equals("todo")) {
                    Task task = parseToDo(input);
                    this.taskList.addTask(task);
                    showTaskUpdates(task);
                } else if (taskType.equals("deadline")) {
                    Task task = parseDeadline(input);
                    this.taskList.addTask(task);
                    showTaskUpdates(task);
                } else if (taskType.equals("event")) {
                    Task task = parseEvent(input);
                    this.taskList.addTask(task);
                    showTaskUpdates(task);
                } else {
                    return "I don't understand";
                }
            } catch (InvalidInputException e) {
                return e.getMessage();
            }
        }
        return this.output.toString();
    }

    public String getStartUp() {
        this.output = new StringBuilder();
        this.output.append(this.ui.showWelcome());
        checkListFile();
        return this.output.toString();
    }

    public String getShutDown() {
        this.output = new StringBuilder();
        try {
            writeListToFile();
        } catch (Exception e) {
            this.output.append("There was a problem writing to the file\n");
        }
        this.output.append(this.ui.exit() + "\n");
        return this.output.toString();
    }

    // Parsers for task creation

    /**
     * Parses the input for creating a ToDo task.
     *
     * @param input the user input
     * @return a new {@code ToDo} task
     * @throws InvalidInputException if the input is invalid
     */
    public Task parseToDo(String input) throws InvalidInputException {
        // invalid input: [todo] or [todo ]
        if (input.length() < 5 || input.substring(5).trim().isEmpty()) {
            this.output.append("invalid input: [todo] or [todo ]\n");
            throw new InvalidInputException();
        }
        String name = input.substring(5);
        return new ToDo(name, false);
    }

    /**
     * Parses the input for creating a Deadline task.
     *
     * @param input the user input
     * @return a new {@code Deadline} task
     * @throws InvalidInputException if the input is invalid
     */
    public Task parseDeadline(String input) throws InvalidInputException {
        // invalid input: [deadline] or [deadline ]
        if (input.length() < 9 || input.substring(9).trim().isEmpty()) {
            this.output.append("invalid input: [deadline] or [deadline ]\n");
            throw new InvalidInputException();
        }
        String[] inputArr = input.substring(9).split(" /by ");
        // invalid input: [deadline *** /by ] or [deadline /by ***]
        if (inputArr.length < 2) {
            this.output.append("invalid input: [deadline *** /by ] or [deadline /by ***]\n");
            throw new InvalidInputException();
        }
        String name = inputArr[0];
        String due = inputArr[1];
        // invalid input: white spaces for name and deadline
        if (name.trim().isEmpty() || due.trim().isEmpty()) {
            this.output.append("invalid input: empty task name or deadline\n");
            throw new InvalidInputException();
        }
        return new Deadline(name, false, due);
    }

    /**
     * Parses the input for creating an Event task.
     *
     * @param input the user input
     * @return a new {@code Event} task
     * @throws InvalidInputException if the input is invalid
     */
    public Task parseEvent(String input) throws InvalidInputException {
        // invalid input: [event] or [event ]
        if (input.length() < 6 || input.substring(6).trim().isEmpty()) {
            this.output.append("invalid input: [event] or [event ]\n");
            throw new InvalidInputException();
        }

        // split [name] /from [rest...]
        String[] inputArr = input.substring(6).split(" /from ");
        // invalid input: [event *** /from ] or [event /from ***]
        if (inputArr.length < 2) {
            this.output.append("invalid input: [event *** /from ] or [event /from ***]\n");
            throw new InvalidInputException();
        }
        input = inputArr[1];
        String name = inputArr[0]; // extract name

        // split [start] /by [end]
        inputArr = input.split(" /to ");
        // invalid input: [event *** /from *** /to ] or [event *** /from /to ***]
        if (inputArr.length < 2) {
            this.output.append("invalid input: [event *** /from *** /to ] or [event *** /from /to ***]\n");
            throw new InvalidInputException();
        }
        String start = inputArr[0];
        String end = inputArr[1];
        if (start.trim().isEmpty() || end.trim().isEmpty()) {
            throw new InvalidInputException();
        }
        return new Event(name, false, start, end);
    }

    // Task management and UI updates

    /**
     * Updates the UI and displays a message after a task is added.
     *
     * @param task the task that was added
     */
    public void showTaskUpdates(Task task) {
        this.output.append("Got it. I've added this task:\n");
        this.output.append("  " + task + "\n");
        this.output.append("Now you have " + taskList.getSize() + " tasks in the list.\n");
    }

    /**
     * Prints the list of tasks to the UI.
     */
    public void printList() {
        this.output.append(" Here are the tasks in your list:\n");
        for (int i = 0; i < this.taskList.getSize(); i++) {
            this.output.append(String.format(" %d.%s", i + 1, this.taskList.getList().get(i)) + "\n");
        }
    }

    /**
     * Marks a task as done or undone.
     *
     * @param i the index of the task
     * @param isDone whether the task should be marked as done
     */
    public void markTask(int i, boolean isDone) {
        Task task = this.taskList.getTask(i);
        task.setIsDone(isDone);
        if (isDone) {
            this.output.append("Nice! I've marked this task as done:\n");
            this.output.append("   " + task + "\n");
        } else {
            this.output.append("OK, I've marked this task as not done yet:\n");
            this.output.append("   " + task + "\n");
        }
    }

    /**
     * Deletes a task from the list.
     *
     * @param i the index of the task to delete
     */
    public void deleteTask(int i) {
        Task task = this.taskList.deleteTask(i);
        this.output.append("Noted. I've removed this task: \n");
        this.output.append("  " + task + "\n");
        this.output.append("Now you have " + this.taskList.getSize() + " tasks in the list.\n");
    }

    /**
     * Finds tasks in the task list that contain the specified keyword (case-insensitive)
     * as a whole word and prints the matching tasks.
     *
     * @param keyword the keyword to search for within the task names.
     *                The search is case-insensitive and matches whole words.
     */
    public void findTask(String keyword) {
        // build list
        ArrayList<Task> resultList = new ArrayList<>();
        String key = keyword.toUpperCase();
        for (Task task : this.taskList.getList()) { // for each task
            String[] nameParts = task.getName().split(" ");
            for (String word : nameParts) { // check through full name
                if (word.toUpperCase().equals(key)) {
                    resultList.add(task);
                }
            }
        }
        // print list
        if (resultList.isEmpty()) {
            this.output.append("There were no matches found.\n");
        } else {
            this.output.append(" Here are the matching tasks in your list:\n");
            for (int i = 0; i < this.taskList.getSize(); i++) {
                this.output.append(String.format(" %d.%s", i + 1, this.taskList.getList().get(i)) + "\n");
            }
        }
    }

    /**
     * Checks if a saved task list file exists and loads it, or creates a new list if no file is found.
     */
    public void checkListFile() {
        boolean isFound = true;
        try {
            readListFile();
        } catch (FileNotFoundException e) {
            this.output.append("No list found\n");
            this.output.append("Creating new list...\n");
            isFound = false;
        } finally {
            if (isFound) {
                printList();
            }
        }
    }

    /**
     * Reads the task list from the storage file and adds tasks to the task list.
     *
     * @throws FileNotFoundException if the storage file is not found
     */
    public void readListFile() throws FileNotFoundException {
        String header = this.storage.readLine();
        while (this.storage.hasNext()) {
            String task = this.storage.readLine();
            try {
                this.taskList.addTask(readTask(task));
            } catch (InvalidInputException e) {
                this.output.append(" There was something wrong with this task.\n");
                this.output.append(" " + task + "\n");
            }
        }
    }

    /**
     * Reads a single task from a line of text and returns the corresponding Task object.
     *
     * @param input the task data in string format
     * @return the corresponding Task object
     * @throws InvalidInputException if the task data is invalid
     */
    public Task readTask(String input) throws InvalidInputException {
        String[] task = input.split(" : ");
        String taskType = task[0];
        if (taskType.equals("T")) {
            if (task.length < 3) {
                this.output.append("len < 3\n");
                throw new InvalidInputException();
            }
            boolean isDone = task[1].equals("1");
            String name = task[2];
            return new ToDo(name, isDone);
        } else if (taskType.equals("D")) {
            if (task.length < 4) {
                this.output.append("len < 4\n");
                throw new InvalidInputException();
            }
            boolean isDone = task[1].equals("1");
            String name = task[2];
            String deadline = task[3];
            return new Deadline(name, isDone, deadline);
        } else if (taskType.equals("E")) {
            if (task.length < 5) {
                this.output.append("len < 5\n");
                throw new InvalidInputException();
            }
            boolean isDone = task[1].equals("1");
            String name = task[2];
            String start = task[3];
            String end = task[4];
            return new Event(name, isDone, start, end);
        } else {
            this.output.append("invalid command\n");
            throw new InvalidInputException();
        }
    }

    /**
     * Writes the current task list to the storage file.
     *
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public void writeListToFile() throws IOException {
        this.storage.clearFile();
        this.output.append("Saving list...\n");
        storage.writeLine(String.format("list: %d", this.taskList.getSize()));
        for (Task task : taskList.getList()) {
            if (task instanceof ToDo) {
                ToDo todo = (ToDo) task;
                this.storage.writeLine(String.format("T : %s : %s", todo.getIsDone() ? "1" : "0", todo.getName()));
            } else if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                this.storage.writeLine(String.format("D : %s : %s : %s",
                        deadline.getIsDone() ? "1" : "0", deadline.getName(),
                        deadline.getDueTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            } else if (task instanceof Event) {
                Event event = (Event) task;
                this.storage.writeLine(String.format("E : %s : %s : %s : %s",
                        event.getIsDone() ? "1" : "0", event.getName(),
                        event.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        event.getEndTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            }
        }
        this.output.append("Saved successfully\n");
    }
}
