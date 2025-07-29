import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class TaskManager {

    private static List<String> tasks;
    private static List<String> completedTasks;
    private static final String DATA_STORAGE_PATH;
    private static final String COMPLETED_TASKS_STORAGE_PATH;

    static {
        String appDataPath = System.getenv("APPDATA");
        String appSpecificDir = appDataPath + File.separator + "ingStudios" + File.separator + "Terminal Task Manager";
        File dataDirectory = new File(appSpecificDir);

        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }

        DATA_STORAGE_PATH = appSpecificDir + File.separator + "stored_tasks.txt";
        COMPLETED_TASKS_STORAGE_PATH = appSpecificDir + File.separator + "completed_tasks.txt";
        
        tasks = loadTasks(DATA_STORAGE_PATH);
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        
        completedTasks = loadTasks(COMPLETED_TASKS_STORAGE_PATH); // Initialize and load completed tasks
        if (completedTasks == null) {
            completedTasks = new ArrayList<>();
        }

        System.out.println("Welcome to the Task Manager!");
        displayTasks();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 5) { // Changed exit choice to 5
            System.out.println("\nChoose a command:");
            System.out.println("1. Create a task");
            System.out.println("2. Delete a task");
            System.out.println("3. Mark a task as complete");
            System.out.println("4. View completed tasks"); // New option to view completed tasks
            System.out.println("5. Exit"); // Corrected exit option
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
                        completeTasks(scanner);
                        break;
                    case 4:
                        displayCompletedTasks(); // Call the new method
                        break;
                    case 5: // Corrected exit case
                        System.out.println("Ending programme...");
                        saveTasks(tasks, DATA_STORAGE_PATH); // Save active tasks
                        saveTasks(completedTasks, COMPLETED_TASKS_STORAGE_PATH); // Save completed tasks
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
        displayTasks();
    }

    public static void deleteTask(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to delete.");
            displayTasks();
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
                displayTasks();
            } else {
                System.out.println("Invalid task number. Please enter a number from the list.");
                displayTasks();
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            displayTasks();
        }
    }

    public static void saveTasks(List<String> taskList, String path) { // Modified to accept list and path
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String task : taskList) {
                writer.write(task);
                writer.newLine();
            }
            System.out.println("Successfully saved tasks to " + path + ".");
        } catch (IOException e) {
            System.err.println("Error saving tasks to " + path + ": " + e.getMessage());
        }
    }

    public static List<String> loadTasks(String path) { // Modified to accept a path
        List<String> loadedList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loadedList.add(line);
            }
            System.out.println("Tasks successfully loaded from " + path);
        } catch (IOException e) {
            System.out.println("No existing tasks found or error loading tasks from " + path + ": " + e.getMessage());
        }
        return loadedList;
    }
    
    public static void displayTasks() {
        System.out.println("\n--- Current Tasks ---");
        if (tasks.isEmpty()) {
            System.out.println("No active tasks.");
        } else {
            for (int count = 0; count < tasks.size(); count++) {
                System.out.println("Task " + (count + 1) + ": " + tasks.get(count));
            }
        }
        System.out.println("---------------------");
    }

    public static void displayCompletedTasks() { // New method to display completed tasks
        System.out.println("\n--- Completed Tasks ---");
        if (completedTasks.isEmpty()) {
            System.out.println("No completed tasks.");
        } else {
            for (int count = 0; count < completedTasks.size(); count++) {
                System.out.println("Completed Task " + (count + 1) + ": " + completedTasks.get(count));
            }
        }
        System.out.println("-----------------------");
    }

    public static void completeTasks(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to mark as complete.");
            displayTasks();
            return;
        }

        System.out.println("\n--- Current Tasks ---");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println("---------------------");

        System.out.print("Enter the number of the task to mark as complete: ");

        if (scanner.hasNextInt()) {
            int taskNumberToComplete = scanner.nextInt();
            scanner.nextLine();

            if (taskNumberToComplete > 0 && taskNumberToComplete <= tasks.size()) {
                String completedTask = tasks.remove(taskNumberToComplete - 1); // Get the task string
                completedTasks.add(completedTask); // Add the task string to completedTasks
                System.out.println("Task marked as complete: \"" + completedTask + "\"");
                displayTasks();
            } else {
                System.out.println("Invalid task number. Please enter a number from the list.");
                displayTasks();
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            displayTasks();
        }
    }
}
