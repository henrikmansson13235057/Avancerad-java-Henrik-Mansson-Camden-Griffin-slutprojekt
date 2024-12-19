package com.example.ToDo.Controller;

import com.example.ToDo.Service.TodoService;
import com.example.ToDo.Models.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TodoController {

    @Autowired
    private TodoService todoService;

    // Get all tasks
    @GetMapping
    public List<Todo> getTasks() {
        return todoService.getAllTasks();
    }

    // Add a new task
    @PostMapping
    public ResponseEntity<Todo> addTask(@RequestBody Todo todo) {
        Todo createdTodo = todoService.createTask(todo);
        return ResponseEntity.ok(createdTodo);
    }

    // Update an existing task
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTask(@PathVariable int id, @RequestBody Todo todo) {
        Todo updatedTodo = todoService.updateTask(id, todo);
        if (updatedTodo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTodo);
    }

    // Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        if (todoService.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
