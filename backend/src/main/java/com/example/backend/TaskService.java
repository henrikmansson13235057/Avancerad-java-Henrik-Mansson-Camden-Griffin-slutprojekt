package com.example.backend;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final List<Task> taskList = new ArrayList<>();

    public void addTask(String name, boolean completed) {
        int id = taskList.size();
        Task task = new Task(id, name, completed);
        taskList.add(task);
    }

    public void deleteTaskById(int id) {
        if (id >= 0 && id < taskList.size()) {
            taskList.remove(id);
            for (int i = 0; i < taskList.size(); i++) {
                taskList.get(i).setId(i);
            }
        }
    }

    public List<Task> getAllTasks() {
        return taskList;
    }

    public void updateTask(int id, Task updatedTask) {
        if (id >= 0 && id < taskList.size()) {
            Task task = taskList.get(id);
            task.setName(updatedTask.getName());
            task.setCompleted(updatedTask.isCompleted());
        }
    }
}
