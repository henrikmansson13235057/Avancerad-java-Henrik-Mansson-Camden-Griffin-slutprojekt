package com.example.forntendtodolist2.service;

import com.example.forntendtodolist2.model.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.hc.client5.http.fluent.Request;

import java.io.IOException;
import java.util.List;

public class TaskService {

    private static final String BASE_URL = "http://localhost:8080/tasks";
    private static final Gson gson = new Gson();

    public static List<Task> getAllTasks() throws IOException {
        String response = Request.get(BASE_URL).execute().returnContent().asString();
        return gson.fromJson(response, new TypeToken<List<Task>>() {}.getType());
    }

    public static Task addTask(Task task) throws IOException {
        String requestBody = gson.toJson(task);
        String response = Request.post(BASE_URL).bodyString(requestBody, org.apache.hc.core5.http.ContentType.APPLICATION_JSON).execute().returnContent().asString();
        return gson.fromJson(response, Task.class);
    }

    public static void deleteTask(int id) throws IOException {
        AbstractStringBuilder Request;
        Request.delete(BASE_URL + "/" + id).execute();
    }
}