package com.example.laboratorio3_20180444_iot.api;

import com.example.laboratorio3_20180444_iot.models.LoginRequest;
import com.example.laboratorio3_20180444_iot.models.LoginResponse;
import com.example.laboratorio3_20180444_iot.models.Todo;
import com.example.laboratorio3_20180444_iot.models.TodoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("todos/user/{userId}")
    Call<TodoResponse> getUserTodos(@Path("userId") int userId);

    @PUT("todos/{id}")
    Call<Todo> updateTodo(@Path("id") int id, @Body Todo todo);
}
