package com.taskmanager;

import com.taskmanager.components.FXMLPath;
import com.taskmanager.model.TaskList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource(FXMLPath.MAIN.getPath()));
//        Parent root = FXMLLoader.load(ClassLoader.getSystemResource(FXMLPath.MAIN.getPath()));

        primaryStage.setTitle("Список усіх задач");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(350);
        primaryStage.setScene(new Scene(root, 600, 350));
        primaryStage.show();
        }

    @Override
    public void stop() throws Exception {
        TaskList.saveTaskList();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
