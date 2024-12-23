package com.example.BackendList.service;


import com.example.BackendList.model.Task;

import java.util.*;

public abstract class TaskService implements BaseService<Task> {
    protected final Map<Integer, Task> taskMap = new HashMap<>();
    protected int currentId = 1;

    @Override
    public List<Task> getAll() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public Task add(Task task) {
        task.setId(currentId++);
        taskMap.put(task.getId(), task);
        return task;
    }

    @Override
    public boolean delete(int id) {
        return taskMap.remove(id) != null;
    }
}
