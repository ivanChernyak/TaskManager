package com.taskmanager.controllers;

import com.taskmanager.model.Task;
import com.taskmanager.model.Tasks;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import com.taskmanager.model.TaskList;

import java.text.ParseException;
import java.util.*;

/**
 * This class is responsible for the logic of working with the calendar.
 */
public class CalendarController extends InputController {
    /**
     * This field is responsible for the table that displays the data.
     */
    @FXML
    private TableView<Map.Entry<Date, String>> tableTaskList;

    /**
     * This field is responsible for the table column that displays the task time.
     */
    @FXML
    private TableColumn<Map.Entry<Date, String>, Date> columnTime;

    /**
     * This field is responsible for the table column that displays task titles.
     */
    @FXML
    private TableColumn<Map.Entry<Date, String>, String> columnTitle;

    private final static Logger LOG = Logger.getLogger(CalendarController.class);

    /**
     * Called to initialize a controller.
     */
    @FXML
    public void initialize() {
        setColumnValueTypes();
        initDatePicker(new DatePicker[]{getDatePickerStart(), getDatePickerEnd()});
    }

    /**
     * Handles the calendar construction event.
     */
    @FXML
    public void actionBuildCalendar() {
        if (validate()) {
            if (validateDateOrder()) {
                try {
                    String textStart = parseTime(getDatePickerStart(), getTextStartHour(), getTextStartMinute());
                    String textEnd = parseTime(getDatePickerEnd(), getTextEndHour(), getTextEndMinute());
                    Date start = getDateFormat().parse(textStart);
                    Date end = getDateFormat().parse(textEnd);
                    SortedMap<Date, Set<Task>> calendar = Tasks.calendar(TaskList.getTasks(), start, end);
                    Map<Date, String> dataInTable = new TreeMap<>();
                    Set<Date> dates = calendar.keySet();
                    for (Date date : dates) {
                        Set<Task> tasksOnDate = calendar.get(date);
                        String taskTitleInTable = getTasksTitlesFromSet(tasksOnDate);
                        dataInTable.put(date, taskTitleInTable);
                    }
                    List<Map.Entry<Date, String>> entrySet = new ArrayList<>(dataInTable.entrySet());
                    ObservableList<Map.Entry<Date, String>> tasks = FXCollections.observableList(entrySet);
                    tableTaskList.setItems(tasks);
                } catch (ParseException e) {
                    LOG.trace("Failed to parse input-date, ParseException: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Checks the validity of the entered data.
     *
     * @return true if data is valid, otherwise return false.
     */
    public boolean validate() {
        return validateTime(getDatePickerStart(), getTextStartHour(), getTextStartMinute())
                && validateTime(getDatePickerEnd(), getTextEndHour(), getTextEndMinute());
    }

    /**
     * Specifies what type of data will be in the table column.
     */
    private void setColumnValueTypes() {
        columnTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Date, String>, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<Map.Entry<Date, String>, Date> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getKey());
            }
        });

        columnTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Date, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Date, String>, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getValue());
            }
        });
    }

    /**
     * Return tasks titles from set on a single string.
     *
     * @param tasks Set with tasks.
     * @return string with tasks titles.
     */
    private String getTasksTitlesFromSet(Set<Task> tasks) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : tasks) {
            stringBuilder.append(task.getTitle()).append(" ");
        }
        return stringBuilder.toString();
    }
}
