package com.taskmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import com.taskmanager.model.TaskList;
import org.apache.log4j.Logger;

/**
 * This class is responsible for the logic of working with the task creating.
 */
public class CreateTaskController extends TaskController {

    private final static Logger LOG = Logger.getLogger(CreateTaskController.class);

    /**
     * Called to initialize a controller.
     */
    @FXML
    public void initialize() {
        this.getAnchorPaneRepeat().setDisable(true);
        initDatePicker(new DatePicker[]{getDatePickerTime(), getDatePickerStart(), getDatePickerEnd()});
    }

    /**
     * Handles the create task event.
     */
    @FXML
    public void actionTask(ActionEvent actionEvent) {
        if (validateTitle(getTextTitle())) {
            if (validate()) {
                TaskList.add(this.getInputTask());
                LOG.info("Task create " + this.getInputTask().toString());
                closeWindow(actionEvent);
            }
        }
    }
}
