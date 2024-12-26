package com.example.frontend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ToDoListApp extends Application {

    private ListView<Task> taskListView;
    private ObservableList<Task> tasks;
    private TextField taskInput;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("App starting...");
        taskListView = new ListView<>();
        tasks = FXCollections.observableArrayList();
        taskListView.setItems(tasks);

        taskListView.setCellFactory(p -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);

                    Label idLabel = new Label(task.getId() + ". ");
                    idLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                    Label taskLabel = new Label(task.getName());

                    Separator separator = new Separator();
                    separator.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
                    HBox.setHgrow(separator, Priority.ALWAYS);

                    Button finishButton = new Button();

                    // set background, text, and font weight based on if its completed or not
                    if (task.isCompleted()) {
                        hbox.setStyle("-fx-background-color: #18b218;");
                        finishButton.setText("Undo completion");
                        taskLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                    } else {
                        hbox.setStyle("-fx-background-color: transparent;");
                        finishButton.setText("Mark as completed");
                        taskLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
                    }

                    finishButton.setOnAction(e -> {
                        finishTask(task);
                        updateItem(task, false);
                    });

                    hbox.getChildren().addAll(idLabel, taskLabel, separator, finishButton);
                    finishButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                    setGraphic(hbox);
                }
            }
        });

        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        Button renameButton = new Button("Rename");
        Button refreshButton = new Button("Refresh all");
        Button clearFinished = new Button("Clear completed");

        taskInput = new TextField();
        taskInput.setPromptText("Enter new task name");
        taskInput.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        taskInput.setStyle("-fx-background-color: #FFFF99; -fx-border-color: grey;");


        addButton.setOnAction(e -> addTask(taskInput.getText()));
        deleteButton.setOnAction(e -> deleteTask());

        renameButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                renameTask(selectedTask);
            } else {
                System.out.println("No task selected.");
            }
        });

        refreshButton.setOnAction(e -> fetchTasks());

        clearFinished.setOnAction(e -> {
            System.out.println("Clearing completed tasks...");
            try {
                URL url = new URL("http://localhost:8080/tasks/completed");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    System.out.println("Tasks removed: " + response + "\n");

                    fetchTasks();
                } else {
                    System.out.println("Failed to clear completed tasks. Response code: " + responseCode);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });




        Label title = new Label("To-Do List");
        title.setAlignment(Pos.CENTER);
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 24));

        HBox buttons = new HBox(addButton, deleteButton, renameButton, refreshButton, clearFinished);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.setStyle("-fx-padding: 10;");

        addButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        deleteButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        renameButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        refreshButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        clearFinished.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        VBox layout = new VBox(10, title, taskInput, buttons, taskListView);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.setStyle("-fx-background-color: #FFFF99;");

        Scene scene = new Scene(layout, 700, 700);
        primaryStage.setTitle("To-Do List");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("App started.\n");

        fetchTasks();
    }

    private void finishTask(Task task) {
        System.out.println("Toggling task completion...");

        task.setCompleted(!task.isCompleted());

        try {
            URL url = new URL("http://localhost:8080/tasks/" + task.getId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String taskJson = "{\"name\":\"" + task.getName() + "\",\"completed\":" + task.isCompleted() + "}";
            connection.getOutputStream().write(taskJson.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Task completion status updated: " + task.getName() + " (" + task.isCompleted() + ")\n");
            } else {
                System.out.println("Failed to update task completion status. Response code: " + responseCode + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchTasks() {
        System.out.println("Fetching tasks...");
        try {
            URL url = new URL("http://localhost:8080/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Task[] fetchedTasks = gson.fromJson(response.toString(), Task[].class);

            tasks.clear();
            tasks.addAll(fetchedTasks);

            System.out.println("Fetched " + fetchedTasks.length + " tasks.\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTask(String taskName) {
        System.out.println("Adding task... ");
        if (!taskName.isEmpty()) {
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

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    Gson gson = new Gson();
                    Task newTask = gson.fromJson(response.toString(), Task.class);

                    // System.out.println("Task: " + newTask);

                    tasks.add(newTask);
                    taskListView.refresh();

                    System.out.println("Task added: " + newTask.getName() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void deleteTask() {
        System.out.println("Deleting task...");
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                URL url = new URL("http://localhost:8080/tasks/" + selectedTask.getId());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {

                    tasks.remove(selectedTask);

                    System.out.println("Task deleted: " + selectedTask.getName() + "\n");
                    fetchTasks();

                } else {
                    System.out.println("Failed to delete task, response code: " + responseCode + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No task selected to delete. \n");
        }
    }


    private void renameTask(Task taskToRename) {
        System.out.println("Renaming task...");
        String renamedTaskName = taskInput.getText();

        if (renamedTaskName != null && !renamedTaskName.isEmpty()) {

            taskToRename.setName(renamedTaskName);


            try {
                URL url = new URL("http://localhost:8080/tasks/" + taskToRename.getId());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);


                String taskJson = "{\"name\":\"" + taskToRename.getName() + "\",\"completed\":" + taskToRename.isCompleted() + "}";
                connection.getOutputStream().write(taskJson.getBytes());

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    taskInput.clear();


                    taskListView.refresh();

                    System.out.println("Task renamed: " + taskToRename.getName() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}