package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class CliRunner implements CommandLineRunner {

    private final TaskService service;

    public CliRunner(TaskService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String cmd = args[0].trim().toLowerCase();
        try {
            switch (cmd) {
                case "add" -> handleAdd(args);
                case "update" -> handleUpdate(args);
                case "delete" -> handleDelete(args);
                case "mark-in-progress" -> handleMark(args, "in-progress");
                case "mark-done" -> handleMark(args, "done");
                case "list" -> handleList(args);
                case "help" -> printHelp();
                default -> {
                    System.err.println("Unknown command: " + cmd);
                    printHelp();
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    private void handleAdd(String[] args) {
        if (args.length < 2) throw new IllegalArgumentException("Usage: task-cli add \"description\"");
        String description = joinFrom(args, 1);
        Task t = service.add(description);
        System.out.println("Task added successfully (ID: " + t.getId() + ")");
    }

    private void handleUpdate(String[] args) {
        if (args.length < 3) throw new IllegalArgumentException("Usage: task-cli update <id> \"new description\"");
        long id = parseId(args[1]);
        String description = joinFrom(args, 2);
        service.update(id, description);
        System.out.println("Task updated successfully (ID: " + id + ")");
    }

    private void handleDelete(String[] args) {
        if (args.length < 2) throw new IllegalArgumentException("Usage: task-cli delete <id>");
        long id = parseId(args[1]);
        service.delete(id);
        System.out.println("Task deleted successfully (ID: " + id + ")");
    }

    private void handleMark(String[] args, String status) {
        if (args.length < 2) {
            String usage = status.equals("in-progress")
                    ? "Usage: task-cli mark-in-progress <id>"
                    : "Usage: task-cli mark-done <id>";
            throw new IllegalArgumentException(usage);
        }
        long id = parseId(args[1]);
        service.markStatus(id, status);
        System.out.println("Task marked as " + status + " (ID: " + id + ")");
    }

    private void handleList(String[] args) {
        String filter = null;
        if (args.length >= 2) {
            filter = args[1].toLowerCase();
            if (!filter.equals("todo") && !filter.equals("in-progress") && !filter.equals("done")) {
                throw new IllegalArgumentException("Usage: task-cli list [todo|in-progress|done]");
            }
        }
        List<Task> tasks = service.list(filter);
        if (tasks.isEmpty()) {
            System.out.println("(no tasks)");
            return;
        }
        for (Task t : tasks) {
            System.out.printf("#%d [%s] %s (createdAt=%s, updatedAt=%s)%n",
                    t.getId(), t.getStatus(), t.getDescription(), t.getCreatedAt(), t.getUpdatedAt());
        }
    }

    private long parseId(String s) {
        try {
            long id = Long.parseLong(s);
            if (id <= 0) throw new NumberFormatException();
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID must be a positive integer.");
        }
    }

    private String joinFrom(String[] arr, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < arr.length; i++) {
            if (i > start) sb.append(' ');
            sb.append(arr[i]);
        }
        return sb.toString().trim();
    }

    private void printHelp() {
        System.out.println("""
          Task Tracker CLI

          Usage:
            task-cli add "Buy groceries"
            task-cli update <id> "Buy groceries and cook dinner"
            task-cli delete <id>
            task-cli mark-in-progress <id>
            task-cli mark-done <id>
            task-cli list
            task-cli list todo
            task-cli list in-progress
            task-cli list done
            task-cli help

          Notes:
            • tasks.json is created/used in the current directory.
            • Status values: todo | in-progress | done
            • IDs are positive integers.
          """);
    }
}
