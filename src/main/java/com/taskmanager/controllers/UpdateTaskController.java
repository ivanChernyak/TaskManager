package com.taskmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import org.apache.log4j.Logger;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskList;

import java.time.ZoneId;

/**
 * This class is responsible for the logic of working with the task updating.
 */
public class UpdateTaskController extends TaskController {

    /**
     * Task that needs to be updated.
     */
    private static Task updateTask;

    private final static Logger LOG = Logger.getLogger(UpdateTaskController.class);

    public static void setUpdateTask(Task updateTask) {
        UpdateTaskController.updateTask = updateTask;
    }

    /**
     * Called to initialize a controller.
     */
    @FXML
    public void initialize() {
        setTaskFields();
        initDatePicker(new DatePicker[]{getDatePickerTime(), getDatePickerStart(), getDatePickerEnd()});
    }

    /**
     * Handles the update task event.
     */
    @Override
    public void actionTask(ActionEvent actionEvent) {
        if (validateTitle(getTextTitle())) {
            if (validate()) {
                TaskList.update(updateTask, this.getInputTask());
                LOG.info("Task update " + this.getInputTask().toString());
                closeWindow(actionEvent);
            }
        }
    }

    /**
     * Sets the data for the task being updated to the view-fields.
     */
    private void setTaskFields() {
        this.getTextTitle().setText(updateTask.getTitle());
        this.getRadioActive().setSelected(updateTask.isActive());
        this.getRadioRepeat().setSelected(updateTask.isRepeated());
        if (!updateTask.isRepeated()) {
            this.getAnchorPaneRepeat().setDisable(true);
            this.getDatePickerTime().setValue(updateTask.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            this.getTextTimeHour().setText(String.format("%tH", updateTask.getTime()));
            this.getTextTimeMinute().setText(String.format("%tM", updateTask.getTime()));
        } else {
            this.getAnchorPaneSimpleTask().setDisable(true);
            this.getDatePickerStart().setValue(updateTask.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            this.getTextStartHour().setText(String.format("%tH", updateTask.getStartTime()));
            this.getTextStartMinute().setText(String.format("%tM", updateTask.getStartTime()));
            this.getDatePickerEnd().setValue(updateTask.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            this.getTextEndHour().setText(String.format("%tH", updateTask.getEndTime()));
            this.getTextEndMinute().setText(String.format("%tM", updateTask.getEndTime()));
            this.getTextIntervalHour().setText(String.valueOf((updateTask.getRepeatInterval() / 60) / 60));
            this.getTextIntervalMinute().setText(String.valueOf((updateTask.getRepeatInterval() / 60) - ((updateTask.getRepeatInterval() / 60) / 60) * 60));
        }
    }
}
