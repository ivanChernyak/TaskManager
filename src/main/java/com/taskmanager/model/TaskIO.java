package com.taskmanager.model;

import com.taskmanager.components.FilePath;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This class contains methods for writing and reading list of tasks
 * in different formats.
 */
public class TaskIO {

    private final static Logger LOG = Logger.getLogger(TaskIO.class);
    private final static File dataDirectory = new File(FilePath.DATA_DIR.getPath());

    /**
     * This method writes tasks from the list to stream in binary format.
     *
     * @param tasks list for writing tasks to the stream.
     * @param out   the stream in which the list is written.
     */
    public static void write(List tasks, OutputStream out) {
        DataOutputStream dataOutput = new DataOutputStream(out);
        try {
            dataOutput.writeInt(tasks.size());
            Iterator iterator = tasks.iterator();
            Task tempTask;
            while (iterator.hasNext()) {
                tempTask = (Task) iterator.next();
                dataOutput.writeUTF(tempTask.getTitle());
                if (tempTask.isActive()) {
                    dataOutput.writeInt(1);
                } else {
                    dataOutput.writeInt(0);
                }
                dataOutput.writeInt(tempTask.getRepeatInterval());
                if (tempTask.isRepeated()) {
                    dataOutput.writeLong(tempTask.getStartTime().getTime());
                    dataOutput.writeLong(tempTask.getEndTime().getTime());
                } else {
                    dataOutput.writeLong(tempTask.getTime().getTime());
                }
            }
        } catch (IOException e) {
            LOG.trace("Failed to write information about task into data-file, IOException: " + e.getMessage());
        } finally {
            try {
                dataOutput.close();
            } catch (IOException e) {
                LOG.trace("Failed to close data-file, IOException: " + e.getMessage());
            }
        }
    }

    /**
     * This method reads tasks from the stream to list in binary format.
     *
     * @param tasks list for recording tasks from the stream.
     * @param in    the stream from which tasks is read.
     */
    public static void read(List<Task> tasks, InputStream in) {
        DataInputStream dataInput = new DataInputStream(in);
        Task tempTask;
        int tempActive = 0;
        boolean isActive;
        boolean isRepeated;
        int interval = 0;
        long time = 0;
        long startTime = 0;
        long endTime = 0;
        String title;
        int i = 0;
        int taskListSize = 0;
        try {
            taskListSize = dataInput.readInt();
            while (i < taskListSize) {
                title = dataInput.readUTF();
                tempActive = dataInput.readInt();
                if (tempActive == 1) {
                    isActive = true;
                } else {
                    isActive = false;
                }
                interval = dataInput.readInt();
                if (interval == 0) {
                    time = dataInput.readLong();
                    isRepeated = false;
                } else {
                    startTime = dataInput.readLong();
                    endTime = dataInput.readLong();
                    isRepeated = true;
                }
                if (!isRepeated) {
                    tempTask = new Task(title, new Date(time));
                } else {
                    tempTask = new Task(title, new Date(startTime),
                            new Date(endTime), interval);
                }
                tempTask.setActive(isActive);
                tasks.add(tempTask);
                i++;
            }
        } catch (IOException e) {
            LOG.trace("Failed to read information about task from data-file, IOException: " + e.getMessage());
        } finally {
            try {
                dataInput.close();
            } catch (IOException e) {
                LOG.trace("Failed to close data-file, IOException: " + e.getMessage());
            }
        }
    }

    /**
     * This method writes tasks from the list to file in binary format.
     *
     * @param tasks list for writing tasks to the file.
     * @param file  the file in which the list is written.
     */
    public static void writeBinary(List tasks, File file) {
        try {
            if (!dataDirectory.exists()) {
                if (dataDirectory.mkdir()) {
                    LOG.info("Data-directory is created: " + dataDirectory.getAbsolutePath());
                } else {
                    LOG.info("Failed to create data-directory: " + dataDirectory.getAbsolutePath());
                }
            }
            write(tasks, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            LOG.trace("Failed to open data-file for writing, IOException: " + e.getMessage());
        }
    }

    /**
     * This method reads tasks from the file to list in binary format.
     *
     * @param tasks list for recording tasks from the file.
     * @param file  the file from which tasks is read.
     */
    public static void readBinary(List<Task> tasks, File file) {
        try {
            if (!dataDirectory.exists()) {
                if (dataDirectory.mkdir()) {
                    LOG.info("Data-directory is created: " + dataDirectory.getAbsolutePath());
                } else {
                    LOG.info("Failed to create data-directory: " + dataDirectory.getAbsolutePath());
                }
            }
            read(tasks, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            LOG.trace("Failed to open data-file for reading, IOException: " + e.getMessage());
        }
    }
}
