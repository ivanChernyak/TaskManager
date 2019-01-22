package com.taskmanager.controllers;

import com.taskmanager.model.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.Date;

/**
 * This class is responsible for the logic of working with the task.
 */
public abstract class TaskController extends InputController {

    /**
     * This field is responsible for the task title.
     */
    @FXML
    private TextField textTitle;

    /**
     * This field is responsible for the task activity.
     */
    @FXML
    private RadioButton radioActive;

    /**
     * This field is responsible for the repeatability of the task.
     */
    @FXML
    private RadioButton radioRepeat;

    /**
     * This field is responsible for the date in a not repeated task.
     */
    @FXML
    private DatePicker datePickerTime;

    /**
     * This field is responsible for the hours in a not repeated task.
     */
    @FXML
    private TextField textTimeHour;

    /**
     * This field is responsible for the minutes in a not repeated task.
     */
    @FXML
    private TextField textTimeMinute;

    /**
     * This field is responsible for the interval minutes in a repeated task.
     */
    @FXML
    private TextField textIntervalMinute;

    /**
     * This field is responsible for the interval hours in a repeated task.
     */
    @FXML
    private TextField textIntervalHour;

    /**
     * This field is responsible for the area on the view
     * with the data-elements for the repeatable task.
     */
    @FXML
    private AnchorPane anchorPaneRepeat;

    /**
     * This field is responsible for the area on the view
     * with the data-elements for the not repeatable task.
     */
    @FXML
    private AnchorPane anchorPaneSimpleTask;

    private final static Logger LOG = Logger.getLogger(TaskController.class);

    public TextField getTextTitle() {
        return textTitle;
    }

    public RadioButton getRadioActive() {
        return radioActive;
    }

    public RadioButton getRadioRepeat() {
        return radioRepeat;
    }

    public DatePicker getDatePickerTime() {
        return datePickerTime;
    }

    public TextField getTextTimeHour() {
        return textTimeHour;
    }

    public TextField getTextTimeMinute() {
        return textTimeMinute;
    }

    public TextField getTextIntervalMinute() {
        return textIntervalMinute;
    }

    public TextField getTextIntervalHour() {
        return textIntervalHour;
    }

    public AnchorPane getAnchorPaneRepeat() {
        return anchorPaneRepeat;
    }

    public AnchorPane getAnchorPaneSimpleTask() {
        return anchorPaneSimpleTask;
    }

    /**
     * Handles the event associated with the action with the task.
     * Overrides in child classes.
     */
    @FXML
    public abstract void actionTask(ActionEvent actionEvent);

    /**
     * Handles an event clicking a radioRepeat button.
     */
    @FXML
    protected void actionRepeat() {
        if (this.getRadioRepeat().isSelected()) {
            this.getAnchorPaneRepeat().setDisable(false);
            this.getAnchorPaneSimpleTask().setDisable(true);
        } else {
            this.getAnchorPaneRepeat().setDisable(true);
            this.getAnchorPaneSimpleTask().setDisable(false);
        }
    }

    /**
     * Creates a task that contains user input data.
     *
     * @return a task that contains user input data.
     */
    protected Task getInputTask() {
        String title = textTitle.getText();
        boolean active = radioActive.isSelected();
        boolean repeated = radioRepeat.isSelected();
        Task task = null;
        try {
            if (!repeated) {
                String textTime = parseTime(getDatePickerTime(), textTimeHour, textTimeMinute);
                Date time = getDateFormat().parse(textTime);
                task = new Task(title, time);
            } else {
                String textStart = parseTime(getDatePickerStart(), getTextStartHour(), getTextStartMinute());
                String textEnd = parseTime(getDatePickerEnd(), getTextEndHour(), getTextEndMinute());
                Date start = getDateFormat().parse(textStart);
                Date end = getDateFormat().parse(textEnd);
                int interval = parseInterval(textIntervalHour, textIntervalMinute);
                task = new Task(title, start, end, interval);
            }
            task.setActive(active);
        } catch (ParseException e) {
            LOG.trace("ParseException: " + e.getMessage());
        }
        return task;
    }

    /**
     * Checks the validity of the entered data.
     *
     * @return true if data is valid, otherwise return false.
     */
    protected boolean validate() {
        if ("".equals(textTitle.getText())) {
            return false;
        }
        if (!radioRepeat.isSelected()) {
            return validateTime(datePickerTime, textTimeHour, textTimeMinute);
        } else {
            return validateTime(getDatePickerStart(), getTextStartHour(), getTextStartMinute())
                    && validateTime(getDatePickerEnd(), getTextEndHour(), getTextEndMinute())
                    && validateInterval(textIntervalHour, textIntervalMinute)
                    && validateDateOrder();
        }
    }

    /**
     * Returns the interval for the repeatable task from user input.
     *
     * @param hour   element with a hours.
     * @param minute element with a minutes.
     * @return
     */
    private int parseInterval(TextField hour, TextField minute) {
        int intervalHour = Integer.parseInt(hour.getText());
        int intervalMinute = Integer.parseInt(minute.getText());
        return intervalHour * 60 * 60 + intervalMinute * 60;
    }
}
