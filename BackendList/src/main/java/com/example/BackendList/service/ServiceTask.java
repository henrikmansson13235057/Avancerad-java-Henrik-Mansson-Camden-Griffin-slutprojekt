package com.example.BackendList.service;

import com.example.BackendList.model.Task;
import org.springframework.stereotype.Service;



@Service
public class ServiceTask extends TaskService {

    @Override
    public Task update(int id, Task updatedTask) {
        Task existingTask = taskMap.get(id);
        if (existingTask != null) {
            existingTask.setName(updatedTask.getName());
            existingTask.setCompleted(updatedTask.isCompleted());
            existingTask.setDueDate(updatedTask.getDueDate());
        }
        return existingTask;
    }
}
