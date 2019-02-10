package com.taskmanager;

import com.taskmanager.components.FXMLPath;
import com.taskmanager.model.TaskList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Main extends Application {
    private final static Logger LOG = Logger.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(FXMLPath.MAIN.getPath()));
        } catch (IOException e) {
            LOG.trace("Failed to load Main-view file, IOException: " + e.getMessage());
            alert();
            Platform.exit();
        }
        primaryStage.setTitle("Список усіх задач");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(350);
        primaryStage.setScene(new Scene(root, 600, 350));
        primaryStage.show();
    }

    @Override
    public void stop() {
        TaskList.saveTaskList();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText("Щось пішло не так :(");
        alert.setContentText("Неможливо запустити програму. Зверніться, будь ласка, до постачальника програми.");
        alert.show();
    }
}
