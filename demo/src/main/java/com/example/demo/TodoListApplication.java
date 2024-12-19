package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class TodoListApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TodoListApplication.class.getResource("/com/example/demo/todolist-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        // add fixed size
        stage.setTitle("To-do list!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}