package com.example.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ToDoListController {

    @FXML
    private ListView<Task> taskListView;
    @FXML
    private TextField taskInput;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;

    private ObservableList<Task> tasks;

    public void initialize() {
        tasks = FXCollections.observableArrayList();
        taskListView.setItems(tasks);

        addButton.setOnAction(e -> addTask());
        deleteButton.setOnAction(e -> deleteTask());
        updateButton.setOnAction(e -> updateTask());
    }

    private void fetchTasks() {
        try {
            URL url = new URL("http://localhost:8080/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTask() {
        String taskName = taskInput.getText();
        if (!taskName.isEmpty()) {
            try {
                URL url = new URL("http://localhost:8080/tasks");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String taskJson = "{\"name\":\"" + taskName + "\",\"completed\":false}";
                connection.getOutputStream().write(taskJson.getBytes());

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    tasks.add(new Task(tasks.size(), taskName, false));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Delete a task from the backend (DELETE request)
    private void deleteTask() {
        Task selectedTask;
        selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                URL url = new URL("http://localhost:8080/tasks/" + selectedTask);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    tasks.remove(selectedTask); // Remove the task from the list view
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Update a task (PUT request)
    private void updateTask() {
        // Get the selected task (which should now be a Task object, not just a String)
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            String updatedTaskName = "Updated Task Name"; // Replace with input from user (e.g., another TextField)

            try {
                // Construct the URL with the correct task ID
                URL url = new URL("http://localhost:8080/tasks/" + selectedTask.getId());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Create the JSON body for the PUT request with the updated task details
                String taskJson = "{\"name\":\"" + updatedTaskName + "\",\"completed\":" + selectedTask.isCompleted() + "}";
                connection.getOutputStream().write(taskJson.getBytes());

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Update the task name in the list
                    selectedTask.setName(updatedTaskName);
                    taskListView.refresh(); // Refresh the ListView to show updated task
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}