package com.example.forntendtodolist2.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Task {
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleBooleanProperty completed = new SimpleBooleanProperty();
    private final ObjectProperty<LocalDate> dueDate = new SimpleObjectProperty<>();

    public Task(String name, boolean completed, LocalDate dueDate) {
        this.name.set(name);
        this.completed.set(completed);
        this.dueDate.set(dueDate);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }
}
