# Task Tracker CLI (Spring Boot + JSON FS)

A simple command-line task tracker that stores tasks in a `tasks.json` file in the **current directory**.

## Features

- Add / Update / Delete tasks
- Mark tasks as `todo`, `in-progress`, or `done`
- List all tasks or filter by status
- JSON persistence in working directory
- No external CLI libs — pure Spring Boot `CommandLineRunner` + Jackson

## Build

Requirements:
- Java 17+
- Maven 3.9+

```bash
mvn clean package

java -jar target/TaskTrackerCLI-1.0-SNAPSHOT.jar help
```

# Usage
### Adding a new task
java TaskCLIApp add "Buy groceries"
### Output: Task added successfully (ID: 1)

### Updating a task
java TaskCLIApp update 1 "Buy groceries and cook dinner"
### Output: Task updated successfully (ID: 1)

### Deleting a task
java TaskCLIApp delete 1
### Output: Task deleted successfully (ID: 1)

### Marking a task as in progress
java TaskCLIApp mark-in-progress 1
### Output: Task marked as in progress (ID: 1)

### Marking a task as done
java TaskCLIApp mark-done 1
### Output: Task marked as done (ID: 1)

### Listing all tasks
java TaskCLIApp list
### Output: List of all tasks

### Listing tasks by status
java TaskCLIApp list todo
java TaskCLIApp list in-progress
java TaskCLIApp list done

