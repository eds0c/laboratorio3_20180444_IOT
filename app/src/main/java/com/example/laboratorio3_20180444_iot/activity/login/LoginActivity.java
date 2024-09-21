package com.example.laboratorio3_20180444_iot.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laboratorio3_20180444_iot.R;
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

        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                // Validación básica: comprobar si los campos están vacíos
                if (user.isEmpty() || pass.isEmpty()) {
                    // Mostrar un mensaje de error si algún campo está vacío
                    Toast.makeText(LoginActivity.this, "Por favor, llena ambos campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Aquí puedes agregar la llamada a la API para verificar las credenciales
                    login(user, pass);

                }
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
                        // Si el inicio de sesión fue exitoso, guarda el token y procede
                        String token = loginResponse.getToken();
                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        // Aquí puedes redirigir a otra actividad
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
