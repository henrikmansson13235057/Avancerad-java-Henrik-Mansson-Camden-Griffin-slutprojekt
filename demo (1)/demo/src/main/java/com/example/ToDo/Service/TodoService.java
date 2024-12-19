package com.example.ToDo.Service;

import com.example.ToDo.Models.Todo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TodoService {

    // In-memory HashMap to store tasks, key is task ID, value is.
    private Map<Integer, Todo> taskMap = new HashMap<>();
    private int idCounter = 1;  // Used to simulate auto-increment for task IDs.

    // Get all tasks
    public List<Todo> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    // Create a new task
    public Todo createTask(Todo todo) {
        todo.setId(idCounter++);
        taskMap.put(todo.getId(), todo);
        return todo;
    }

    // Update an existing task
    public Todo updateTask(int id, Todo todo) {
        if (taskMap.containsKey(id)) {
            todo.setId(id);  // Ensure the ID is preserved when updating
            taskMap.put(id, todo);
            return todo;
        }
        return null;  // Return null if the task doesn't exist
    }

    // Delete a task
    public boolean deleteTask(int id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
            return true;
        }
        return false;
    }
}
