package com.example.forntendtodolist2;

import com.example.forntendtodolist2.model.Task;
import com.example.forntendtodolist2.service.TaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class HelloController {

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> nameColumn;
    @FXML private TableColumn<Task, Boolean> completedColumn;
    @FXML private TableColumn<Task, LocalDate> dueDateColumn;

    @FXML private TextField nameField;
    @FXML private DatePicker dueDatePicker;
    @FXML private CheckBox completedCheckBox;

    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        completedColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        // Bind the observable list to the table
        taskTable.setItems(taskList);

        // Load initial tasks
        loadTasks();
    }

    private void loadTasks() {
        try {
            taskList.setAll(TaskService.getAllTasks());
        } catch (Exception e) {
            showAlert("Error", "Failed to load tasks: " + e.getMessage());
        }
    }

    @FXML
    private void addTask() {
        String name = nameField.getText();
        LocalDate dueDate = dueDatePicker.getValue();
        boolean completed = completedCheckBox.isSelected();

        if (name == null || name.isEmpty() || dueDate == null) {
            showAlert("Validation Error", "Name and Due Date are required.");
            return;
        }

        Task newTask = new Task(name, completed, dueDate);
        try {
            Task addedTask = TaskService.addTask(newTask);
            taskList.add(addedTask);
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add task: " + e.getMessage());
        }
    }

    @FXML
    private void deleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert("Validation Error", "No task selected.");
            return;
        }

        try {
            TaskService.deleteTask(selectedTask.getId());
            taskList.remove(selectedTask);
        } catch (Exception e) {
            showAlert("Error", "Failed to delete task: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.clear();
        dueDatePicker.setValue(null);
        completedCheckBox.setSelected(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
