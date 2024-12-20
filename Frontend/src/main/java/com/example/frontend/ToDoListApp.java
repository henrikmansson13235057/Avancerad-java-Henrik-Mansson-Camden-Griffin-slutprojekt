package com.example.frontend;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ToDoListApp extends Application {

    private ListView<String> taskListView;
    private ObservableList<String> tasks;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create UI components
        taskListView = new ListView<>();
        tasks = FXCollections.observableArrayList();
        taskListView.setItems(tasks);

        Button addButton = new Button("Add Task");
        Button deleteButton = new Button("Delete Task");
        Button updateButton = new Button("Update Task");

        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter task name");

        // Set up event handlers
        addButton.setOnAction(e -> addTask(taskInput.getText()));
        deleteButton.setOnAction(e -> deleteTask());
        updateButton.setOnAction(e -> updateTask());

        VBox layout = new VBox(10, taskInput, addButton, deleteButton, updateButton, taskListView);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("To-Do List");
        primaryStage.setScene(scene);
        primaryStage.show();

        fetchTasks();
    }

    private void fetchTasks() {
        // Fetch tasks from the backend (GET /tasks)
        try {
            URL url = new URL("http://localhost:8080/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse response and update task list (this should be a JSON response, so you might want a library like Gson or Jackson)
            // For simplicity, we're directly adding hardcoded tasks in this example

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTask(String taskName) {
        // Send a POST request to add a new task
        try {
            URL url = new URL("http://localhost:8080/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String taskJson = "{\"name\":\"" + taskName + "\",\"completed\":false}";
            connection.getOutputStream().write(taskJson.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                tasks.add(taskName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTask() {
        // Handle task deletion
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                URL url = new URL("http://localhost:8080/tasks/" + selectedTask);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    tasks.remove(selectedTask);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTask() {
        // Handle task update
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            String updatedTaskName = "Updated Task Name"; // Update with a new name
            try {
                URL url = new URL("http://localhost:8080/tasks/0"); // Modify with correct task ID
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String taskJson = "{\"name\":\"" + updatedTaskName + "\",\"completed\":false}";
                connection.getOutputStream().write(taskJson.getBytes());

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    tasks.set(tasks.indexOf(selectedTask), updatedTaskName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
