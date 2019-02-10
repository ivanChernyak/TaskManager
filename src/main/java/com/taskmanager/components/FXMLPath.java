package com.taskmanager.components;

/**
 * This enum is responsible for the paths to the views FXML-files.
 */
public enum FXMLPath {
    CALENDAR("/views/CalendarView.fxml"),
    CREATE("/views/CreateTaskView.fxml"),
    DELETE("/views/DeleteTaskView.fxml"),
    UPDATE("/views/UpdateTaskView.fxml"),
    MAIN("/views/MainView.fxml");

    private String path;

    FXMLPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
