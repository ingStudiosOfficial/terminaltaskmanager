import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TaskManager {

    // 1. Make tasks list a static member of the class
    // This allows both main and other static methods to access it.
    // Initialize it directly by calling loadTasks()
    private static List<String> tasks; // Declare it first
    private static final String DATA_STORAGE_PATH = "stored_tasks.txt";

    // Static initializer block to load tasks when the class is first loaded
    static {
        tasks = loadTasks(); // Call loadTasks to initialize the list
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Task Manager!");
        // 2. Create only ONE Scanner object for System.in
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 3) {
            System.out.println("\nChoose a command:"); // Added newline for better formatting
            System.out.println("1. Create a task");
            System.out.println("2. Delete a task");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: "); // Added prompt for clarity

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                // CRITICAL: Consume the leftover newline after nextInt()
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        // 3. Call the static createTask method and pass the scanner
                        createTask(scanner);
                        break;
                    case 2:
                        // You'll need to implement deleteTask similarly
                        deleteTask(scanner); // Call the delete task method
                        break;
                    case 3:
                        System.out.println("Ending programme...");
                        saveTasks(); // Save tasks before exiting
                        break;
                    default:
                        System.out.println("Input is not valid, please try again.");
                        break;
                }
            } else {
                System.out.println("Invalid input. Please select a valid integer.");
                // CRITICAL: Consume the invalid input to prevent infinite loop
                scanner.next();
            }
        }
        // 4. Close the scanner ONLY when the program is done with all input
        scanner.close();
        System.out.println("Goodbye!"); // Confirm exit
    }

    // Make createTask static so it can be called from main
    // Pass the existing scanner object to avoid creating new ones
    public static void createTask(Scanner scanner) {
        System.out.print("Enter task name: "); // Use print for input prompt on same line
        String taskName = scanner.nextLine(); // Read the full line for task name
        tasks.add(taskName);
        System.out.println("Task added: \"" + taskName + "\"");
    }

    // Add a deleteTask method (also static)
    public static void deleteTask(Scanner scanner) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to delete.");
            return; // Exit the method if no tasks
        }

        System.out.println("\n--- Current Tasks ---");
        // Print tasks with their numbers
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println("---------------------");

        System.out.print("Enter the number of the task to delete: ");

        if (scanner.hasNextInt()) {
            int taskNumberToDelete = scanner.nextInt();
            scanner.nextLine(); // Consume newline after reading int

            if (taskNumberToDelete > 0 && taskNumberToDelete <= tasks.size()) {
                String removedTask = tasks.remove(taskNumberToDelete - 1); // -1 because List is 0-indexed
                System.out.println("Task deleted: \"" + removedTask + "\"");
            } else {
                System.out.println("Invalid task number. Please enter a number from the list.");
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume invalid non-numeric input
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

    // Corrected: loadTasks now returns a List<String>
    public static List<String> loadTasks() {
        List<String> loadedList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_STORAGE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loadedList.add(line);
            }
            System.out.println("Tasks successfully loaded from " + DATA_STORAGE_PATH);
        } catch (IOException e) {
            // This message is fine if the file doesn't exist on first run
            System.out.println("No existing tasks found or error loading tasks: " + e.getMessage());
        }
        return loadedList; // Return the List directly
    }
}