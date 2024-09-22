package com.example.laboratorio3_20180444_iot.models;

public class LoginResponse {
    private String token;
    private String firstName;  // Nombre
    private String lastName;   // Apellido
    private String email;
    private String gender;
    private int id;// Correo

    // Constructor vacío, getters y setters
    public LoginResponse() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }
}
