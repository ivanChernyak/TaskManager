package com.taskmanager.notification;

import com.taskmanager.model.Task;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.log4j.Logger;
import com.taskmanager.controllers.MainController;
import com.taskmanager.model.TaskList;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * This class is responsible for the logic of working with the tasks notifications.
 */
public class Notification implements Runnable {
    /**
     * This field is responsible for tasks list.
     */
    private static List<Task> list = TaskList.getTasks();

    /**
     * An instance of the class that contains the method
     * for displaying the alert window.
     */
    private MainController mainController;

    private final static Logger LOG = Logger.getLogger(Notification.class);

    public Notification(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        check();
                    }
                });
            } catch (InterruptedException e) {
                LOG.trace("Some thread interrupted the current thread, InterruptedException: " + e.getMessage());
            }
        }
    }

    /**
     * Checks the task list for the task that should be executed now.
     * If such a task is in the list, it displays an alert window.
     */
    private void check() {
        for (Task task : list) {
            Instant now = new Date().toInstant().truncatedTo(ChronoUnit.SECONDS);
            Instant time = task.getTime().toInstant().truncatedTo(ChronoUnit.SECONDS);
            if (now.equals(time)) {
                mainController.alert("Сповіщення",
                        "Сповіщення",
                        "Зараз має виконатися задача " + task.getTitle(),
                        Alert.AlertType.INFORMATION);
            }
        }
    }
}
