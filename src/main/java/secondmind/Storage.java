package secondmind;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Storage {
    private final String DATA_FILE_PATH;

    public Storage(String DATA_FILE_PATH) {
        this.DATA_FILE_PATH = DATA_FILE_PATH;
    }

    public void appendToFile(String data, int taskCount) throws IOException {
        File f = new File(DATA_FILE_PATH);
        FileWriter fw = new FileWriter(f, true);
        fw.write(data + "\n");
        fw.close();
    }

    public void updateTaskInDataFile(int taskNumber, boolean done, int taskCount) throws FileNotFoundException, IOException {
        int lineNumber = 1;
        File oldFile = new File(DATA_FILE_PATH);
        File newFile = new File("./tempDataFile.txt");
        FileWriter fw = new FileWriter("./tempDataFile.txt", true);
        Scanner s = new Scanner(oldFile);
        while (s.hasNext()) {
            String currentLine = s.nextLine();
            if (lineNumber == taskNumber) {
                StringBuilder sb = new StringBuilder();
                //Add task type and "|" to stringbuilder
                sb.append(currentLine.substring(0,2));
                if (done) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
                //Add rest of line
                sb.append(currentLine.substring(3));
                if (lineNumber < taskCount) {
                    fw.write(sb.toString() + "\n");
                } else {
                    fw.write(sb.toString());
                }
                lineNumber++;
                continue;
            }
            if (lineNumber < taskCount) {
                fw.write(currentLine + "\n");
            } else {
                fw.write(currentLine);
            }
            lineNumber++;
        }
        s.close();
        fw.close();
        newFile.renameTo(oldFile);
    }

    public void delete(int taskNumber, int taskCount) throws InvalidTaskNumberException, FileNotFoundException, IOException {
        if (taskNumber <= 0 || taskNumber > taskCount) {
            throw new InvalidTaskNumberException(taskNumber);
        }
        //Remove line "taskNumber" from data file
        int lineNumber = 1;
        File oldFile = new File(DATA_FILE_PATH);
        File newFile = new File("./tempDataFile.txt");
        FileWriter fw = new FileWriter("./tempDataFile.txt", true);
        Scanner s = new Scanner(oldFile);
        while (s.hasNext()) {
            String currentLine = s.nextLine();
            if (lineNumber == taskNumber) {
                lineNumber++;
                continue;
            }
            if (lineNumber < taskCount) {
                fw.write(currentLine + "\n");
            } else {
                fw.write(currentLine);
            }
            lineNumber++;
        }
        s.close();
        fw.close();
        newFile.renameTo(oldFile);
    }

    private Task textToTask(String text) {
        String[] taskInfo = text.split("\\|");
        String taskType = taskInfo[0];
        Task curr;
        if (taskType.equals("T")) {
            curr = new ToDoTask(taskInfo[2]);
        } else if (taskType.equals("D")) {
            curr = new DeadlineTask(taskInfo[2], taskInfo[3]);
        } else {
            curr = new EventTask(taskInfo[2], taskInfo[3], taskInfo[4]);
        }
        if (taskInfo[1].equals("1")) {
            curr.markAsDone();
        }
        return curr;
    }

    public ArrayList<Task> loadTaskList() throws FileNotFoundException, IOException {
        ArrayList<Task> taskList = new ArrayList<>();
        File f = new File(DATA_FILE_PATH);
        if (!f.exists()) {
            f.createNewFile();
        }
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            Task curr = textToTask(s.nextLine());
            taskList.add(curr);
        }
        return taskList;
    }
}