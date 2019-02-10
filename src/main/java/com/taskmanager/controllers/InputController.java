package com.taskmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * This class is responsible for the logic of working with the user input data.
 */
public abstract class InputController {

    /**
     * This field is responsible for the starting date in a repeated task or when building a calendar.
     */
    @FXML
    private DatePicker datePickerStart;

    /**
     * This field is responsible for the starting hours in a repeated task or when building a calendar.
     */
    @FXML
    private TextField textStartHour;

    /**
     * This field is responsible for the starting minutes in a repeated task or when building a calendar.
     */
    @FXML
    private TextField textStartMinute;

    /**
     * This field is responsible for the ending date in a repeated task or when building a calendar.
     */
    @FXML
    private DatePicker datePickerEnd;

    /**
     * This field is responsible for the ending hours in a repeated task or when building a calendar.
     */
    @FXML
    private TextField textEndHour;

    /**
     * This field is responsible for the ending minutes in a repeated task or when building a calendar.
     */
    @FXML
    private TextField textEndMinute;

    /**
     * This field is responsible for the date format used in the task.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static Logger LOG = Logger.getLogger(InputController.class);

    public static SimpleDateFormat getDateFormat() {
        return DATE_FORMAT;
    }

    public DatePicker getDatePickerStart() {
        return datePickerStart;
    }

    public TextField getTextStartHour() {
        return textStartHour;
    }

    public TextField getTextStartMinute() {
        return textStartMinute;
    }

    public DatePicker getDatePickerEnd() {
        return datePickerEnd;
    }

    public TextField getTextEndHour() {
        return textEndHour;
    }

    public TextField getTextEndMinute() {
        return textEndMinute;
    }

    /**
     * Handles an event clicking a Cancel button.
     * Closes the current window.
     *
     * @param actionEvent some type of action.
     */
    @FXML
    public void actionClose(ActionEvent actionEvent) {
        this.closeWindow(actionEvent);
    }

    /**
     * Shows notification window.
     *
     * @param message message that should be shown.
     */
    public void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Попедедження");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Checks the validity of the entered data.
     *
     * @return true if data is valid, otherwise return false.
     */
    protected abstract boolean validate();

    /**
     * Changes the state of availability of fields for entering
     * the end date when creating a repeated task or when building a calendar.
     */
    @FXML
    protected void setEnableEndFields() {
        if (datePickerStart.getValue() == null) {
            datePickerEnd.setDisable(true);
            textEndHour.setDisable(true);
            textEndMinute.setDisable(true);
        } else {
            datePickerEnd.setDisable(false);
            textEndHour.setDisable(false);
            textEndMinute.setDisable(false);
        }
    }

    /**
     * Creates a day cell factory for the DatePicker to disable
     * the cells corresponding to the preceding dates.
     *
     * @param datePickers elements to which the factory should be applied.
     */
    protected void initDatePicker(DatePicker[] datePickers) {
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(LocalDate.now())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffd7d8;");
                                }
                            }
                        };
                    }
                };
        for (DatePicker datePicker : datePickers) {
            datePicker.setDayCellFactory(dayCellFactory);
        }
    }

    /**
     * Checks the order of the entered data for the repeated task
     * or when building a calendar.
     *
     * @return true if order is valid, otherwise return false.
     */
    protected boolean validateDateOrder() {
        String textStart = parseTime(datePickerStart, textStartHour, textStartMinute);
        String textEnd = parseTime(datePickerEnd, textEndHour, textEndMinute);
        Date start = null;
        Date end = null;
        try {
            start = DATE_FORMAT.parse(textStart);
            end = DATE_FORMAT.parse(textEnd);
        } catch (ParseException e) {
            LOG.trace("Failed to parse input-date, ParseException: " + e.getMessage());
        }
        if (start.before(end))
            return true;
        else {
            alert("Оберіть правильний порядок дат!");
            return false;
        }
    }

    /**
     * Closes the current window.
     *
     * @param actionEvent some type of action.
     */
    protected void closeWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Returns the date and time that the user entered as a string.
     *
     * @param datePicker element with a date.
     * @param hour       element with a hours.
     * @param minute     element with a minutes.
     * @return string representation of date and time.
     */
    protected String parseTime(DatePicker datePicker, TextField hour, TextField minute) {
        StringBuilder time = new StringBuilder();
        time.append(datePicker.getValue())
                .append(" ")
                .append(hour.getText())
                .append(":")
                .append(minute.getText())
                .append(":0");
        return time.toString();
    }

    /**
     * Checks the validity of the entered data.
     *
     * @param datePicker element with a date.
     * @param hour       element with a hours.
     * @param minute     element with a minutes.
     * @return true if data is valid, otherwise return false.
     */
    protected boolean validateTime(DatePicker datePicker, TextField hour, TextField minute) {
        if (datePicker.getValue() == null) {
            alert("Виберіть дату!");
            return false;
        } else {
            return validateInterval(hour, minute);
        }
    }

    /**
     * Checks the validity of the entered data.
     *
     * @param hour   element with a hours.
     * @param minute element with a minutes.
     * @return true if data is valid, otherwise return false.
     */
    protected boolean validateInterval(TextField hour, TextField minute) {
        if ((hour.getText()).matches("\\d{2}") && (minute.getText()).matches("\\d{2}")) {
            if (Integer.parseInt(hour.getText()) < 24 && Integer.parseInt(hour.getText()) >= 0) {
                if (Integer.parseInt(minute.getText()) < 60 && Integer.parseInt(minute.getText()) >= 0) {
                    return true;
                } else {
                    alert("Хвилини мають бути числами в діапазоні від 00 до 59!");
                    return false;
                }
            } else {
                alert("Години мають бути числами в діапазоні від 00 до 23!");
                return false;
            }

        } else {
            alert("Години й хвилини мають бути числами!");
            return false;
        }
    }

    protected boolean validateTitle(TextField textTitle) {
        if ("".equals(textTitle.getText())) {
            alert("Введіть назву задачі!");
            return false;
        } else {
            if (textTitle.getLength() > 30) {
                alert("Назва задачі має бути менше 30 символів!");
                return false;
            } else {
                return true;
            }
        }
    }
}
