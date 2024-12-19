package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TodoListController {

    @FXML
    private DatePicker dueDate;

    @FXML
    private Button listAdd;

    @FXML
    private Button listClear;

    @FXML
    private ScrollPane listDate;

    @FXML
    private Button listRefresh;

    @FXML
    private Button listRemove;

    @FXML
    private TextField todoDescription;

    @FXML
    private Button todoDone;

    @FXML
    private Button todoFinish;

    @FXML
    private Label todoNumber;

    // Method to add a task
    @FXML
    void addTask(ActionEvent event) {
        try {
            String description = todoDescription.getText().trim();
            String dueDateText = dueDate.getValue() != null ? dueDate.getValue().toString() : "";

            // Validate input
            if (description.isEmpty() || dueDateText.isEmpty()) {
                todoNumber.setText("Please fill in the task description and due date.");
                return;
            }

            postTask(description, dueDateText);

        } catch (Exception e) {
            todoNumber.setText("Unexpected Error: " + e.getMessage());
        }
    }

    // Method to clear finished tasks
    @FXML
    void clearFinished(ActionEvent event) {
        // Logic to clear finished tasks (you can implement as needed)
        todoNumber.setText("Clearing finished tasks...");
    }

    // Method to get all tasks (could call an API or load from local data)
    @FXML
    void getTasks(ActionEvent event) {
        // Logic to fetch tasks from the server or local data
        todoNumber.setText("Fetching tasks...");
    }

    // Method to remove a task
    @FXML
    void listRemove(ActionEvent event) {
        // Logic to remove the selected task from the list or backend
        todoNumber.setText("Removing task...");
    }

    // Method to mark a task as finished
    @FXML
    void toFinished(ActionEvent event) {
        // Logic to mark a task as finished (can update UI or notify the server)
        todoNumber.setText("Task marked as finished.");
    }

    // Method to send the task data to the server
    private void postTask(String description, String dueDate) {
        try {
            URL url = new URL("http://localhost:8080/api/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create a task object
            Task task = new Task(description, dueDate);

            // Convert the task object to JSON (if you need to implement Gson)
            // String json = gson.toJson(task);

            // Send the JSON data to the server
            try (OutputStream os = connection.getOutputStream()) {
                // os.write(json.getBytes("UTF-8"));
                os.flush();
            }

            // Handle server response
            int responseCode = connection.getResponseCode();
            String response = readResponse(connection);

            if (responseCode >= 200 && responseCode < 300) {
                todoNumber.setText("Task successfully added!\n" + response);
            } else {
                todoNumber.setText("Error: Failed to add task. Server response:\n" + response);
            }
        } catch (IOException e) {
            todoNumber.setText("Error: Unable to connect to server. " + e.getMessage());
        }
    }

    // Helper method to read the server response
    private String readResponse(HttpURLConnection connection) throws IOException {
        try (java.io.InputStreamReader reader = new java.io.InputStreamReader(connection.getInputStream());
             java.io.BufferedReader in = new java.io.BufferedReader(reader)) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    public void removeTask(ActionEvent actionEvent) {
    }

    // Task class to represent the task object
    class Task {
        private String description;
        private String dueDate;

        public Task(String description, String dueDate) {
            this.description = description;
            this.dueDate = dueDate;
        }

        public String getDescription() {
            return description;
        }

        public String getDueDate() {
            return dueDate;
        }
    }
}
