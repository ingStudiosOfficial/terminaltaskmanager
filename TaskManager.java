import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File; // Import File class

public class TaskManager {

    private static List<String> tasks;
    private static final String DATA_STORAGE_PATH;

    static {
        String appDataPath = System.getenv("APPDATA");
        String appSpecificDir = appDataPath + File.separator + "ingStudios" + File.separator + "Terminal Task Manager";
        File dataDirectory = new File(appSpecificDir);

        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }

        DATA_STORAGE_PATH = appSpecificDir + File.separator + "stored_tasks.txt";
        tasks = loadTasks();
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Task Manager!");
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 3) {
            System.out.println("\nChoose a command:");
            System.out.println("1. Create a task");
            System.out.println("2. Delete a task");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        createTask(scanner);
                        break;
                    case 2:
                        deleteTask(scanner);
                        break;
                    case 3:
                        System.out.println("Ending programme...");
                        saveTasks();
                        break;
                    default:
                        System.out.println("Input is not valid, please try again.");
                        break;
                }
            } else {
                System.out.println("Invalid input. Please select a valid integer.");
                scanner.next();
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }

    public static void createTask(Scanner scanner) {
        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();
        tasks.add(taskName);
        System.out.println("Task added: \"" + taskName + "\"");
    }

    public static void deleteTask(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to delete.");
            return;
        }

        System.out.println("\n--- Current Tasks ---");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println("---------------------");

        System.out.print("Enter the number of the task to delete: ");

        if (scanner.hasNextInt()) {
            int taskNumberToDelete = scanner.nextInt();
            scanner.nextLine();

            if (taskNumberToDelete > 0 && taskNumberToDelete <= tasks.size()) {
                String removedTask = tasks.remove(taskNumberToDelete - 1);
                System.out.println("Task deleted: \"" + removedTask + "\"");
            } else {
                System.out.println("Invalid task number. Please enter a number from the list.");
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
    }

    public static void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_STORAGE_PATH))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
            System.out.println("Successfully saved tasks to " + DATA_STORAGE_PATH + ".");
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    public static List<String> loadTasks() {
        List<String> loadedList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_STORAGE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loadedList.add(line);
            }
            System.out.println("Tasks successfully loaded from " + DATA_STORAGE_PATH);
        } catch (IOException e) {
            System.out.println("No existing tasks found or error loading tasks: " + e.getMessage());
        }
        return loadedList;
    }
}
