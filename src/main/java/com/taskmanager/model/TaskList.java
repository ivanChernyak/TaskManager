package com.taskmanager.model;

import com.taskmanager.components.FilePath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.ArrayList;

public class TaskList {
    /**
     * Responsible for the file in which the task list is stored.
     */
    private static final File file =  new File(FilePath.DATA_FILE.getPath());

    /**
     * Responsible for storing the task list while the program is running.
     */
    private static ObservableList<Task> tasks = FXCollections.observableArrayList(new ArrayList<Task>());

    public static ObservableList<Task> getTasks() {
        return tasks;
    }

    /**
     * Reads a task list from a file.
     */
    public static void loadTaskList() {
        TaskIO.readBinary(tasks, file);
    }

    /**
     * Saves the task list to a file
     */
    public static void saveTaskList() {
        TaskIO.writeBinary(tasks, file);
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param task element to be appended to this list.
     */
    public static void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the first occurrence of the specified element from list,
     * if it is present.
     *
     * @param task element to be removed from this list, if present.
     * @return true if this list contained the specified element.
     */
    public static boolean remove(Task task) {
        return tasks.remove(task);
    }

    /**
     * Changes the specified element to a new one.
     *
     * @param updateTask element to be removed from this list.
     * @param newTask    element to be appended to this list.
     */
    public static void update(Task updateTask, Task newTask) {
        remove(updateTask);
        add(newTask);
    }
}
