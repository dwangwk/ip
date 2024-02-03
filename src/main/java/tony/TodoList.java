package tony;

import tony.exceptions.InvalidTaskException;
import tony.tasks.Task;
import tony.tasks.TaskType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of tasks in the Tony application.
 */
public class TodoList {
    List<Task> list = new ArrayList<>();

    /**
     * Adds a task to the task list.
     *
     * @param item The task to be added.
     */
    public void add(Task item) {
        list.add(item);
        int numberOfTasks = list.size();
        line();
        System.out.println("Got it dawg. I've added this task: \n");
        System.out.println(item.toString() + "\n");
        System.out.println("Now you got "+ numberOfTasks + " tony.tasks fam \n");
        line();
    }

    /**
     * Marks a task as done based on the provided input.
     *
     * @param input The input representing the task to mark as done.
     */
    public void mark(String input) {
        try {
            int index = Integer.parseInt(input);
            list.get(index - 1).markAsDone();
            line();
            System.out.println("Marked item " + index + " as done dawg.");
            line();
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            line();
            System.out.println("Invalid input for 'mark' command dawg.");
            line();
        }
    }

    /**
     * Unmarks a previously marked task as undone based on the provided input.
     *
     * @param input The input representing the task to unmark as done.
     */
    public void unmark(String input) {
        try {
            int index = Integer.parseInt(input);
            list.get(index - 1).markAsUndone();
            line();
            System.out.println("Unmarked item " + index + " as done.");
            line();
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            line();
            System.out.println("Invalid input for 'unmark' command.");
            line();
        }
    }

    /**
     * Deletes a task from the task list based on the provided input.
     *
     * @param input The input representing the task to delete.
     */
    public void delete(String input) {
        try {
            int index = Integer.parseInt(input);
            String task = list.get(index - 1).toString();
            list.remove(index - 1);
            line();
            System.out.println("Deleted item: \n" + task + "\n");
            System.out.println("Now you have " + list.size() + " tony.tasks left in the list.");
            line();
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            line();
            System.out.println("Invalid input for 'delete' command.");
            line();
        }
    }

    /**
     * Prints the list of tasks.
     */
    public void print() {
        line();
        System.out.println("Here are the tony.tasks in your list: \n");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString() + "\n");
        }
        line();
    }

    /**
     * Returns a formatted string representation of all tasks in the list for storage.
     *
     * @return A formatted string representing all tasks in the list.
     */
    public String printTasksToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).formattedString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Loads a task from a given line of text and adds it to the list.
     *
     * @param line The line of text representing a task.
     */
    public void loadTask(String line) throws InvalidTaskException {
        Task task = createTaskFromLine(line);
        list.add(task);
    }

    /**
     * Function to create a Task from it's formatted String representation
     *
     * @param line The line of text representing a task.
     * @return Task object
     */
    private Task createTaskFromLine(String line) throws InvalidTaskException {
        String[] parts = line.split("\\|");

        if (parts.length >= 3) {
            String taskType = parts[0];
            int completionStatus = Integer.parseInt(parts[1]);
            String taskDetails = parts[2];

            if (taskType.equals("T")) {
                Task todo = new TaskFactory().createTask(TaskType.TODO, taskDetails);
                if (completionStatus == 1) {
                    todo.markAsDone();
                }
                return todo;
            } else if (taskType.equals("D")) {
                String deadlineDate = parts[3];
                Task deadline = new TaskFactory().createTask(TaskType.DEADLINE, taskDetails, "by: " + deadlineDate);
                if (completionStatus == 1) {
                    deadline.markAsDone();
                }
                return deadline;
            } else if (taskType.equals("E")) {
                Task event = new TaskFactory().createTask(TaskType.EVENT, taskDetails, "from: " + parts[3], "to:" + parts[4]);
                if (completionStatus == 1) {
                    event.markAsDone();
                }
                return event;
            }
        }
        return null;
    }

    private static void line() {
        System.out.println("_______________________\n");
    }

    public void find(String input) {
        int count = 1;
        List<String> output = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            String description = task.getDescription();
            if (description.contains(input)) {
                output.add("" + count + ". " + task.toString()+ "\n");
                count++;
            }
        }
        if (count == 1) {
            line();
            System.out.println("Sorry there are no tasks matching " + input + "\n");
            line();
        } else {
            line();
            System.out.println("Here are the matching tasks in your list: \n");
            for (int i = 0; i < count - 1; i++) {
                System.out.println(output.get(i));
            }
            line();
        }
    }
}