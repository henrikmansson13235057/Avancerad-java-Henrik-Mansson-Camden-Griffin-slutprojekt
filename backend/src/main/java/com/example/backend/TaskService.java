package com.example.backend;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final List<Task> taskList = new ArrayList<>();
    private int nextId = 1;

    public Task addTask(String name, boolean completed) {
        Task task = new Task(nextId++, name, completed);
        taskList.add(task);
        return task;
    }

    public void deleteTaskById(int id) {
        taskList.removeIf(task -> task.getId() == id);
        reassignIds();
    }

    public int deleteCompletedTasks() {
        int initialSize = taskList.size();
        taskList.removeIf(Task::isCompleted);
        reassignIds();
        return initialSize - taskList.size();
    }

    public List<Task> getAllTasks() {
        return taskList;
    }

    public void updateTask(int id, Task updatedTask) {
        Task task = taskList.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
        if (task != null) {
            task.setName(updatedTask.getName());
            task.setCompleted(updatedTask.isCompleted());
        }
    }

    private void reassignIds() {
        nextId = 1;
        for (Task task : taskList) {
            task.setId(nextId++);
        }
    }
}

