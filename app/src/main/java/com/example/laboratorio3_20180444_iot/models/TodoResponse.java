package com.example.laboratorio3_20180444_iot.models;

import java.util.List;

public class TodoResponse {
    private List<Todo> todos;

    // Getters y Setters
    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }
}
