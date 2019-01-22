package com.taskmanager.controllers;

import com.taskmanager.components.FXMLPath;
import com.taskmanager.model.Task;
import com.taskmanager.notification.Notification;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import com.taskmanager.model.TaskList;

import java.io.IOException;
import java.util.Date;

/**
 * This class is responsible for the logic of working with the main application window.
 */
public class MainController {
    /**
     * This field is responsible for the table that displays the data for all tasks.
     */
    @FXML
    private TableView<Task> tableTaskList;

    /**
     * This field is responsible for the table column that displays the task title.
     */
    @FXML
    private TableColumn<Task, String> columnTitle;

    /**
     * This field is responsible for the table column that displays the task time.
     */
    @FXML
    private TableColumn<Task, Date> columnTime;

    /**
     * This field is responsible for the table column that displays the task activity.
     */
    @FXML
    private TableColumn<Task, Boolean> columnActive;

    /**
     * This field is responsible for the table column
     * that displays the task start time for the repeatable task.
     */
    @FXML
    private TableColumn<Task, Date> columnStart;

    /**
     * This field is responsible for the table column
     * that displays the task end time for the repeatable task.
     */
    @FXML
    private TableColumn<Task, Date> columnEnd;

    /**
     * This field is responsible for the table column
     * that displays the task interval for the repeatable task.
     */
    @FXML
    private TableColumn<Task, Integer> columnInterval;

    private final static Logger LOG = Logger.getLogger(MainController.class);

    /**
     * Called to initialize a controller.
     * Load tasks from file to TaskList.
     * Starts a thread with notifications.
     */
    @FXML
    public void initialize() {
        TaskList.loadTaskList();
        tableTaskList.setItems(TaskList.getTasks());
        setColumnValueTypes();
        Thread thread = new Thread(new Notification(this));
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Handles the task update event.
     */
    @FXML
    public void updateTask() {
        Task selectedTask = tableTaskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            UpdateTaskController.setUpdateTask(selectedTask);
            showView("Редагувати задачу", FXMLPath.UPDATE);
        } else {
            alert("Попедедження",
                    "Ви не вибрали задачу!",
                    "Оберіть, будь ласка, задачу для редагування!",
                    Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles the task create event.
     */
    @FXML
    public void addTask() {
        showView("Додати задачу", FXMLPath.CREATE);
    }

    /**
     * Handles the calendar construction event.
     */
    @FXML
    public void buildCalendar() {
        showView("Переглянути календар", FXMLPath.CALENDAR);
    }

    /**
     * Handles the task delete event.
     */
    @FXML
    public void deleteTask() {
        Task selectedTask = tableTaskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            DeleteTaskController.setDeleteTask(selectedTask);
            showView("Видалити задачу", FXMLPath.DELETE);
        } else {
            alert("Попедедження",
                    "Ви не вибрали задачу!",
                    "Оберіть, будь ласка, задачу для видалення!",
                    Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows notification window.
     *
     * @param message message that should be shown.
     */
    public void alert(String title, String headerText, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a window that is responsible for one of the application features.
     *
     * @param sceneTitle window title.
     * @param resource   view-file path.
     */
    private void showView(String sceneTitle, FXMLPath resource) {
        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(ClassLoader.getSystemResource(resource.getPath()));
        loader.setLocation(getClass().getResource(resource.getPath()));
        try {
            loader.load();
        } catch (IOException e) {
            LOG.trace("IOException: " + e.getMessage());
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle(sceneTitle);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * Specifies what type of data will be in the table column.
     */
    private void setColumnValueTypes() {
        columnTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Task, String> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getTitle());
            }
        });
        columnTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<Task, Date> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getTime());
            }
        });
        columnActive.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Task, Boolean> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().isActive());
            }
        });
        columnStart.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<Task, Date> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getStartTime());
            }
        });
        columnEnd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TableColumn.CellDataFeatures<Task, Date> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getEndTime());
            }
        });
        columnInterval.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Task, Integer> param) {
                return new ReadOnlyObjectWrapper<>(param.getValue().getRepeatInterval());
            }
        });
    }
}
