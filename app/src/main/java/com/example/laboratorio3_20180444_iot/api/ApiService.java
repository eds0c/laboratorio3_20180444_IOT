package com.example.laboratorio3_20180444_iot.api;

import com.example.laboratorio3_20180444_iot.models.LoginRequest;
import com.example.laboratorio3_20180444_iot.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ApiService {
    @FormUrlEncoded
    @POST("/login")
    public Call<LoginResponse> login(@Body LoginRequest loginRequest) {
        return null;
    }
}
