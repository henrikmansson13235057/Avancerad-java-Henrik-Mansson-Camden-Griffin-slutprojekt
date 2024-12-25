package com.example.frontend;

import com.google.gson.annotations.Expose;

public class Task {
    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private boolean completed;

    // task constructor
    public Task(int id, String name, boolean completed) {
        this.id = id;
        this.name = name;
        this.completed = completed;
    }

    // getters and setters
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

    // overrides toString method
    @Override
    public String toString() {
        return id + ". " + name;
    }
}
