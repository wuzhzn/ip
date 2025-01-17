package duke.storage;

import duke.parser.Parser;
import duke.task.Task;
import duke.tasklist.TaskList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Format of tasks in txt file:
 * For todo task:
 * T | (status) | task-description
 * For deadline task:
 * D | (status) | task-description | by
 * For event task:
 * E | (status) | task-description | start | end
 *
 * where status can be marked or unmarked (1 / 0 )
 */

/**
 * Storage that handles the data from text file
 */
public class Storage {

    private static final String dirPath = "data";
    private static final String filePath = "data/duke.txt";
    private static ArrayList<Task> tasks = new ArrayList<>();


    /**
     * Check for file access and load the data from the text file
     *
     * @return the task list saved on the file
     */
    public static ArrayList<Task> storageInit() {
        try {
            checkFileAccess();
            load();
        } catch (FileNotFoundException err) {
            System.out.println("File not Found");
        } catch (IOException err) {
            System.out.println("Something went wrong: " + err.getMessage());
        }
        return tasks;
    }
    /**
     * Updates tasklist with data from existing save file if it exists
     *
     * @throws FileNotFoundException if unable to access the text file
     */
    public static void load() throws FileNotFoundException {
        File file = new File(filePath); // create a File for the given file path
        Scanner s = new Scanner(file); // create a Scanner using the File as the source
        while (s.hasNext()) {
            String textLine = s.nextLine();
            tasks.add(Parser.parseTextFile(textLine));
        }
    }

    public static void writeToFile(String textToAdd) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(textToAdd);
        fw.close();
    }

    public static void appendToFile(String textToAppend) throws IOException {
        FileWriter fw = new FileWriter(filePath, true);
        fw.write(textToAppend);
        fw.close();
    }

    /**
     * Creates a new directory and text file if it does not exist
     *
     * @throws IOException if error occurs during file creation/access checking
     */
    public static void checkFileAccess() throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File f = new File(filePath);
        if (!f.exists()) {
            f.createNewFile();
        }
    }

    /**
     * Updates the text file with data from the tasklist
     *
     * @param tasks the task-list that contains all the tasks
     */
    public static void updateFile(TaskList tasks) {
        ArrayList<Task> tasksList = tasks.returnTasks();
        try {
            writeToFile("");
            for (Task task : tasksList) {
                appendToFile(task.textToSave() + System.lineSeparator());
            }
        } catch (IOException err) {
            System.out.println("Something went wrong: " + err.getMessage());
        }
    }
}