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
     * Processes the user input and returns an appropriate response.
     *
     * This method determines the command from the input string and performs the corresponding action,
     * such as adding tasks, marking tasks, deleting tasks, or displaying help information.
     * It also handles system exit commands and other actions.
     *
     * @param input the user's input command as a string.
     * @return a string response based on the command and its execution result.
     */
    public String getResponse(String input) {
        this.output = new StringBuilder();
        if (input == null || input.trim().isEmpty()) {
            return "No input detected";
        }
        // determine command
        String[] inputArr = input.split(" ");
        String command = inputArr[0].toLowerCase();
        if (command.equals("help")) {
            this.output.append("list of commands:\n");
            this.output.append("todo     : add todo task\n");
            this.output.append("deadline : add deadline task\n");
            this.output.append("event    : add event task\n");
            this.output.append("list     : show list of all tasks\n");
            this.output.append("mark n   : mark task n as done\n");
            this.output.append("unmark n : unmark task n as not done\n");
            this.output.append("delete n : delete task n from list\n");
            this.output.append("find     : search list by keyword\n");
            this.output.append("sort     : show list in sorted order\n");
            this.output.append("sort a   : show sorted list and apply sort\n");
            this.output.append("bye      : save list and exit program\n");
        } else if (command.equals("bye")) {
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
            boolean isApply = true;
            try {
                isApply = !inputArr[1].isEmpty();
            } catch (ArrayIndexOutOfBoundsException e) {
                isApply = false;
            }
            showSortedList(isApply);
        } else { // add tasks
            handleAddTask(input);
        }
        return this.output.toString();
    }

    /**
     * Prepares and returns the startup message.
     *
     * This method initializes the response with a welcome message and ensures the
     * task list file is checked and loaded if available.
     *
     * @return the startup message as a string.
     */
    public String getStartUp() {
        this.output = new StringBuilder();
        this.output.append(this.ui.showWelcome());
        checkListFile();
        return this.output.toString();
    }

    /**
     * Prepares and returns the shutdown message.
     *
     * This method writes the current task list to a file and appends the goodbye message
     * to the response.
     *
     * @return the shutdown message as a string.
     */
    public String getShutDown() {
        this.output = new StringBuilder();
        writeListToFile();
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
            //this.output.append("Todo format: [todo] [name]\n");
            throw new InvalidInputException("Todo format: [todo] [name]");
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
            this.output.append("Deadline format: deadline [name] /by [DD/MM/YYYY HH:MM]\n");
            throw new InvalidInputException("Deadline format: deadline [name] /by [DD/MM/YYYY HH:MM]\n");
        }
        String[] inputArr = input.substring(9).split(" /by ");
        // invalid input: [deadline *** /by ] or [deadline /by ***]
        if (inputArr.length < 2) {
            //this.output.append("Deadline format: deadline [name] /by [DD/MM/YYYY HH:MM]\n");
            throw new InvalidInputException("Deadline format: deadline [name] /by [DD/MM/YYYY HH:MM]\n");
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
            //this.output.append("Event format: event [name] /from [DD/MM/YYYY HH:MM] /to [DD/MM/YYYY HH:MM]\n");
            throw new InvalidInputException("Event format: event [name] "
                    + "/from [DD/MM/YYYY HH:MM] /to [DD/MM/YYYY HH:MM]\n");
        }

        // split [name] /from [rest...]
        String[] inputArr = input.substring(6).split(" /from ");
        // invalid input: [event *** /from ] or [event /from ***]
        if (inputArr.length < 2) {
            //this.output.append("Event format: event [name] /from [DD/MM/YYYY HH:MM] /to [DD/MM/YYYY HH:MM]\n");
            throw new InvalidInputException("Event format: event [name] "
                    + "/from [DD/MM/YYYY HH:MM] /to [DD/MM/YYYY HH:MM]\n");
        }
        input = inputArr[1];
        String name = inputArr[0]; // extract name

        // split [start] /by [end]
        inputArr = input.split(" /to ");
        // invalid input: [event *** /from *** /to ] or [event *** /from /to ***]
        if (inputArr.length < 2) {
            //this.output.append("Event format: event [name] /from [DD/MM/YYYY HH:MM] /to [DD/MM/YYYY HH:MM]\n");
            throw new InvalidInputException("Event format: event [name] "
                    + "/from [DD/MM/YYYY HH:MM] /to [DD/MM/YYYY HH:MM]\n");
        }
        String start = inputArr[0];
        String end = inputArr[1];

        assert !name.trim().isEmpty() : "name should not be empty";
        assert !start.trim().isEmpty() : "start time should not be empty";
        assert !end.trim().isEmpty() : "end time should not be empty";

        return new Event(name, false, start, end);
    }

    // Task management and UI updates
    /**
     * Handles the addition of a new task based on the user input.
     *
     * This method determines the type of task (ToDo, Deadline, or Event) from the input string
     * and adds it to the task list after parsing. If the input does not match any recognized
     * task type or is invalid, an appropriate error message is appended to the output.
     *
     * @param input the string input from the user specifying the task type and details.
     * @throws InvalidInputException if the input is invalid and cannot be parsed into a task.
     */
    public void handleAddTask(String input) {
        String taskType = input.split(" ")[0].toLowerCase();
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
     * Checks if the given index is within the bounds of the task list.
     *
     * If the index is out of bounds (greater than or equal to the size of the task list),
     * an error message is appended to the output, and the method returns false.
     * Otherwise, the method returns true.
     *
     * @param i the index to check.
     * @return {@code true} if the index is valid (i.e., less than the size of the task list),
     *         {@code false} otherwise.
     */
    public boolean checkIndex(int i) {
        if (i >= taskList.getSize()) {
            this.output.append("There are only " + taskList.getSize() + " tasks in the list");
            return false;
        } else {
            return true;
        }
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
        if (!checkIndex(i)) {
            return;
        }
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
        if (!checkIndex(i)) {
            return;
        }
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
            mergeSort(array, mid + 1, high); // then recursively sort them
            merge(array, low, mid, high); // conquer: the merge routine
        }
    }

    /**
     * Displays the sorted list of tasks.
     *
     * If the `apply` parameter is set to {@code true}, the current task list is sorted directly,
     * and the sorted list is displayed. If `apply` is set to {@code false}, a cloned copy of
     * the task list is sorted and displayed, leaving the original list unchanged.
     *
     * The sorting is performed using the `mergeSort` method.
     *
     * @param apply a boolean flag indicating whether the sort should be applied directly to
     *              the current task list. If {@code true}, the original list is sorted;
     *              if {@code false}, a clone of the list is sorted.
     */
    public void showSortedList(boolean apply) {
        // clone list
        if (apply) {
            ArrayList<Task> currList = this.taskList.getList();
            mergeSort(currList, 0, this.taskList.getSize() - 1);
            printList(currList);
        } else {
            ArrayList<Task> cloneList = new ArrayList<>();
            cloneList.addAll(this.taskList.getList());
            mergeSort(cloneList, 0, cloneList.size() - 1);
            printList(cloneList);
        }
    }

    /**
     * Checks if a saved task list file exists and loads it, or creates a new list if no file is found.
     */
    public void checkListFile() {
        boolean isFound = readListFile();
        if (isFound) {
            printList(this.taskList.getList());
        }
    }

    /**
     * Reads the task list from the storage file and adds tasks to the task list.
     *
     * @throws FileNotFoundException if the storage file is not found
     */
    public boolean readListFile() {
        String header = this.storage.readLine();
        if (header.isEmpty()) {
            return false;
        }
        while (this.storage.hasNext()) {
            String task = this.storage.readLine();
            try {
                this.taskList.addTask(readTask(task));
            } catch (InvalidInputException e) {
                this.output.append(" There was something wrong with this task.\n");
                this.output.append(" " + task + "\n");
            }
        }
        return true;
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
            throw new InvalidInputException("invalid command\n");
        }
    }

    /**
     * Reads input to create a ToDo task.
     *
     * This method parses the input array to extract information required to construct a ToDo task.
     * It checks for the presence of sufficient arguments and throws an exception if the input is invalid.
     *
     * @param inputArr an array of strings representing the input for a ToDo task.
     *                 The expected format is: {"todo", "isDone", "name"}.
     * @return a {@code ToDo} task constructed from the input arguments.
     * @throws InvalidInputException if the input array has insufficient arguments.
     */
    public Task readToDo(String[] inputArr) throws InvalidInputException {
        if (inputArr.length < 3) {
            //this.output.append("len < 3\n");
            throw new InvalidInputException("insufficient arguments");
        }
        boolean isDone = inputArr[1].equals("1");
        String name = inputArr[2];
        return new ToDo(name, isDone);
    }

    /**
     * Reads input to create a Deadline task.
     *
     * This method parses the input array to extract information required to construct a Deadline task.
     * It checks for the presence of sufficient arguments and throws an exception if the input is invalid.
     *
     * @param inputArr an array of strings representing the input for a Deadline task.
     *                 The expected format is: {"deadline", "isDone", "name", "deadline"}.
     * @return a {@code Deadline} task constructed from the input arguments.
     * @throws InvalidInputException if the input array has insufficient arguments.
     */
    public Task readDeadline(String[] inputArr) throws InvalidInputException {
        if (inputArr.length < 4) {
            //this.output.append("len < 4\n");
            throw new InvalidInputException("insufficient arguments");
        }
        boolean isDone = inputArr[1].equals("1");
        String name = inputArr[2];
        String deadline = inputArr[3];
        return new Deadline(name, isDone, deadline);
    }

    /**
     * Reads input to create an Event task.
     *
     * This method parses the input array to extract information required to construct an Event task.
     * It checks for the presence of sufficient arguments and throws an exception if the input is invalid.
     *
     * @param inputArr an array of strings representing the input for an Event task.
     *                 The expected format is: {"event", "isDone", "name", "start", "end"}.
     * @return an {@code Event} task constructed from the input arguments.
     * @throws InvalidInputException if the input array has insufficient arguments.
     */
    public Task readEvent(String[] inputArr) throws InvalidInputException {
        if (inputArr.length < 5) {
            //this.output.append("len < 5\n");
            throw new InvalidInputException("insufficient arguments");
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
    public void writeListToFile() {
        try {
            if (this.storage.hasWriteFile()) {
                this.storage.clearFile();
            }
            this.output.append("Saving list...\n");
            storage.writeLine(String.format("list: %d", this.taskList.getSize()));
            int numTasks = taskList.getSize();
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
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
