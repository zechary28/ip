# Task Manager Chatbot

## Overview
The Task Manager Chatbot is an interactive tool to help users efficiently manage their tasks. It supports creating, managing, and sorting tasks with simple commands. The chatbot supports three types of tasks:

- **ToDo**: A basic task.
- **Deadline**: A task with a due time.
- **Event**: A task with a start time and end time.

## Features
- Add tasks of different types (ToDo, Deadline, Event).
- Mark or unmark tasks as completed.
- Delete tasks.
- Find tasks by keywords.
- Sort tasks by type or completion status.
- Save tasks and exit the program.

## Commands
Below is the list of commands you can use to interact with the chatbot:

### Task Commands
- **`todo`**: Add a ToDo task. Example: `todo Buy groceries`
- **`deadline`**: Add a Deadline task. Example: `deadline Submit report /by 2025/02/22 23:59`
- **`event`**: Add an Event task. Example: `event Team meeting /from 2025/02/23 14:00 /to 2025-02-23 16:00`

### Task Management
- **`list`**: Show all tasks in the current list.
- **`mark n`**: Mark task `n` as completed. Example: `mark 2`
- **`unmark n`**: Unmark task `n` as not completed. Example: `unmark 2`
- **`delete n`**: Delete task `n` from the list. Example: `delete 3`

### Search and Sort
- **`find keyword`**: Search tasks by keyword. Example: `find meeting`
- **`sort`**: Show the list of tasks in sorted order without applying the sort.
- **`sort a`**: Show the list of tasks in sorted order and apply the sort.

### Program Control
- **`bye`**: Save the task list to a file and exit the program.

## Example Usage
```
> todo Read a book

Added: [T][ ] Read a book

> deadline Submit assignment /by 2025-02-22
Added: [D][ ] Submit assignment (by: 2025-02-22)

> list
1. [T][ ] Read a book
2. [D][ ] Submit assignment (by: 2025-02-22)

> mark 1
Marked task 1 as done: [T][X] Read a book

> bye
Tasks saved. Goodbye!
```

## System Requirements
- **Java Version**: Ensure you have Java 17 or later installed.

## Setup and Running
1. Download the luke.jar file
2. Move the jar file to your desired directory
3. Navigate to the jar file in your directory.
4. Run the chatbot:
   ```
   java -jar luke.jar
   ```
   

## Saving and Loading Tasks
- Tasks are automatically saved when you use the `bye` command.
- The chatbot will load saved tasks upon startup if a task file exists.

## Contributions
Contributions are welcome! Please fork the repository and submit a pull request for review.

---
Feel free to enhance this chatbot by adding more features or improving its existing functionality!

## Credits
Background by [helloimjimthecat](https://www.vecteezy.com/vector-art/14022441-water-pokemon-pattern)