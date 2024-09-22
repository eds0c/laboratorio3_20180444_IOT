package com.example.laboratorio3_20180444_iot.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laboratorio3_20180444_iot.R;
import com.example.laboratorio3_20180444_iot.activity.timer.TimerActivity;
import com.example.laboratorio3_20180444_iot.api.ApiClient;
import com.example.laboratorio3_20180444_iot.api.ApiService;
import com.example.laboratorio3_20180444_iot.models.LoginRequest;
import com.example.laboratorio3_20180444_iot.models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.Username);
        password = findViewById(R.id.password);
        loginbutton = findViewById(R.id.login_button);

        loginbutton.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            // Validación básica: comprobar si los campos están vacíos
            if (user.isEmpty() || pass.isEmpty()) {
                // mostrar un mensaje de error si algún campo está vacío
                Toast.makeText(LoginActivity.this, "Por favor, llena ambos campos", Toast.LENGTH_SHORT).show();
            } else {
                // llamamos a la API para el logueo
                login(user, pass);

            }
        });
    }

    private void login(String username, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        //si el inicio de sesión fue exitoso, guarda el token y procede
                        String token = loginResponse.getToken();
                        String userName = loginResponse.getFirstName();
                        String userLastName = loginResponse.getLastName();
                        String userEmail = loginResponse.getEmail();
                        String userGender = loginResponse.getGender();
                        int userId = loginResponse.getId();
                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, TimerActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userLastName", userLastName);
                        intent.putExtra("userEmail", userEmail);
                        intent.putExtra("gender", userGender);
                        intent.putExtra("id", userId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Manejar errores de conexión
                Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
