package com.taskmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskList;

/**
 * This class is responsible for the logic of working with the task deleting.
 */
public class DeleteTaskController {

    /**
     * This field is responsible for the delete-task title.
     */
    @FXML
    private Label deleteTaskTitle;

    /**
     * Task that needs to be deleted.
     */
    private static Task deleteTask;

    private final static Logger LOG = Logger.getLogger(DeleteTaskController.class);

    public static void setDeleteTask(Task deleteTask) {
        DeleteTaskController.deleteTask = deleteTask;
    }

    /**
     * Handles the delete task event.
     */
    @FXML
    void deleteTask(ActionEvent event) {
        TaskList.remove(deleteTask);
        LOG.info("Task remove " + deleteTask.toString());
        this.closeWindow(event);
    }

    /**
     * Handles an event clicking a Cancel button.
     * Closes the current window.
     *
     * @param actionEvent some type of action.
     */
    @FXML
    void cancelDelete(ActionEvent actionEvent) {
        this.closeWindow(actionEvent);
    }

    @FXML
    public void initialize() {
        deleteTaskTitle.setText(deleteTask.getTitle());
    }

    /**
     * Closes the current window.
     *
     * @param actionEvent some type of action.
     */
    private void closeWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
