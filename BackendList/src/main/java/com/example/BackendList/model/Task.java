package com.example.BackendList.model;

import java.time.LocalDate;

public class Task {
    private int id;
    private String name;
    private boolean completed;
    private LocalDate dueDate;

    // Default constructor for Jackson
    public Task() {}

    public Task(int id, String name, boolean completed, LocalDate dueDate) {
        this.id = id;
        this.name = name;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}

