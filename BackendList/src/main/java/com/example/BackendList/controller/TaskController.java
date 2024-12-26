package com.example.BackendList.controller;

import com.example.BackendList.model.Task;
import com.example.BackendList.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAll();
    }

    @PostMapping
    public Task addTask(@RequestBody Task task) {
        return taskService.add(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable int id, @RequestBody Task task) {
        return taskService.update(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable int id) {
        taskService.delete(id);
    }
}
