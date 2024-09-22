package com.example.laboratorio3_20180444_iot.models;

import androidx.annotation.NonNull;

public class Todo {
    private int id;
    private String todo;
    private boolean completed;
    private String userId;
    private String username;

    // Constructor
    public Todo(int id, String todo, boolean completed, String userId, String username) {
        this.id = id;
        this.todo = todo;
        this.completed = completed;
        this.userId = userId;
        this.username = username;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getTodo() {
        return todo;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    @NonNull
    @Override
    public String toString() {
        return todo + " - " + (completed ? "Completado" : "No completado");
    }

}
