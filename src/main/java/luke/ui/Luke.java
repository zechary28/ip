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
            printList(this.taskList.getList());
        } else if (command.equals("delete")) {
            deleteTask(Integer.parseInt(inputArr[1]) - 1);
        } else if (command.equals("find")) {
            findTask(inputArr[1]);
        } else if (command.equals("sort")) {
            showSortedList();
        } else { // add tasks
            handleAddTask(input);
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
        this.output.append(this.ui.exit()).append("\n");
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

        assert !name.trim().isEmpty() : "name should not be empty";

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

        assert !name.trim().isEmpty() : "name should not be empty";
        assert !due.trim().isEmpty() : "deadline should not be empty";

        // invalid input: white spaces for name and deadline
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

        assert !name.trim().isEmpty() : "name should not be empty";
        assert !start.trim().isEmpty() : "start time should not be empty";
        assert !end.trim().isEmpty() : "end time should not be empty";

        return new Event(name, false, start, end);
    }

    // Task management and UI updates

    public void handleAddTask(String input) {
        String taskType = input.split(" ")[0];
        try {
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
                this.output.append("I don't understand");
            }
        } catch (InvalidInputException e) {
            this.output.append(e.getMessage());
        }
    }

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
    public void printList(ArrayList<Task> tasks) {
        this.output.append(" Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            this.output.append(String.format(" %d.%s", i + 1, tasks.get(i)) + "\n");
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
            String upperCaseTaskName = task.getName().toUpperCase();
            String upperCaseKey = keyword.toUpperCase();
            if (upperCaseTaskName.contains(upperCaseKey)) {
                resultList.add(task);
            }
        }
        // print list
        if (resultList.isEmpty()) {
            this.output.append("There were no matches found.\n");
        } else {
            printList(resultList);
        }
    }

    // Sorting
    private void merge(ArrayList<Task> array, int low, int mid, int high) {
        // subarray1 = array[low..mid], subarray2 = array[mid+1..high], both sorted
        int left = low;
        int right = mid + 1;
        ArrayList<Task> backupList = new ArrayList<>();

        // Merging both subarrays
        while (left <= mid && right <= high) {
            Task earlierTask = array.get(left).compareTo(array.get(right)) < 0
                    ? array.get(left++)
                    : array.get(right++);
            backupList.add(earlierTask);
        }

        // Copy any remaining elements from the left subarray
        while (left <= mid) {
            backupList.add(array.get(left++));
        }

        // Copy any remaining elements from the right subarray
        while (right <= high) {
            backupList.add(array.get(right++));
        }

        // Copy the sorted elements back into the original array
        for (int i = 0; i < backupList.size(); i++) {
            array.set(low + i, backupList.get(i));
        }
    }

    private void mergeSort(ArrayList<Task> array, int low, int high) {
        // the array to be sorted is array[low..high]
        if (low < high) { // base case: low >= high (0 or 1 item)
            int mid = (low + high) / 2;
            mergeSort(array, low, mid); // divide into two halves
            mergeSort(array,mid + 1, high); // then recursively sort them
            merge(array, low, mid, high); // conquer: the merge routine
        }
    }

    public void showSortedList() {
        // clone list
        ArrayList<Task> cloneList = new ArrayList<>();
        cloneList.addAll(this.taskList.getList());
        mergeSort(cloneList, 0, cloneList.size() - 1);
        printList(cloneList);
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
                printList(this.taskList.getList());
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
        String[] taskParts = input.split(" : ");
        String taskType = taskParts[0];
        if (taskType.equals("T")) {
            return readToDo(taskParts);
        } else if (taskType.equals("D")) {
            return readDeadline(taskParts);
        } else if (taskType.equals("E")) {
            return readEvent(taskParts);
        } else {
            this.output.append("invalid command\n");
            throw new InvalidInputException();
        }
    }

    public Task readToDo(String[] inputArr) throws InvalidInputException {
        if (inputArr.length < 3) {
            this.output.append("len < 3\n");
            throw new InvalidInputException();
        }
        boolean isDone = inputArr[1].equals("1");
        String name = inputArr[2];
        return new ToDo(name, isDone);
    }

    public Task readDeadline(String[] inputArr) throws InvalidInputException {
        if (inputArr.length < 4) {
            this.output.append("len < 4\n");
            throw new InvalidInputException();
        }
        boolean isDone = inputArr[1].equals("1");
        String name = inputArr[2];
        String deadline = inputArr[3];
        return new Deadline(name, isDone, deadline);
    }

    public Task readEvent(String[] inputArr) throws InvalidInputException {
        if (inputArr.length < 5) {
            this.output.append("len < 5\n");
            throw new InvalidInputException();
        }
        boolean isDone = inputArr[1].equals("1");
        String name = inputArr[2];
        String start = inputArr[3];
        String end = inputArr[4];
        return new Event(name, isDone, start, end);
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
                this.storage.writeLine(String.format("T : %s : %s",
                        todo.getIsDone() ? "1" : "0",
                        todo.getName()));
            } else if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                this.storage.writeLine(String.format("D : %s : %s : %s",
                        deadline.getIsDone() ? "1" : "0",
                        deadline.getName(),
                        deadline.getDueTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            } else if (task instanceof Event) {
                Event event = (Event) task;
                this.storage.writeLine(String.format("E : %s : %s : %s : %s",
                        event.getIsDone() ? "1" : "0",
                        event.getName(),
                        event.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        event.getEndTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            }
        }
        this.output.append("Saved successfully\n");
    }
}
