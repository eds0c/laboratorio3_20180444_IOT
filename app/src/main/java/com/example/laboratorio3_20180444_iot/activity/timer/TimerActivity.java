package com.example.laboratorio3_20180444_iot.activity.timer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.laboratorio3_20180444_iot.R;
import com.example.laboratorio3_20180444_iot.activity.TodoActivity;
import com.example.laboratorio3_20180444_iot.activity.login.LoginActivity;
import com.example.laboratorio3_20180444_iot.api.ApiClient;
import com.example.laboratorio3_20180444_iot.api.ApiService;
import com.example.laboratorio3_20180444_iot.models.Todo;
import com.example.laboratorio3_20180444_iot.models.TodoResponse;
import com.google.gson.Gson;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TimerActivity extends AppCompatActivity {
    private TextView tvTimer;
    private ImageButton btnPlay;
    private ImageButton btnLogout;
    private TimerViewModel timerViewModel;

    private List<Todo> todos = new ArrayList<>(); // Lista para almacenar los Todo
    private String todosJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        tvTimer = findViewById(R.id.tvTimer);
        btnPlay = findViewById(R.id.btnPlay);
        btnLogout = findViewById(R.id.btnLogout);

        TextView tvUserName = findViewById(R.id.tvUserName);
        TextView tvUserLastName = findViewById(R.id.tvUserLastName);
        TextView tvUserEmail = findViewById(R.id.tvUserEmail);
        ImageView ivGender = findViewById(R.id.ivGender);

        String userName = getIntent().getStringExtra("userName");
        String userLastName = getIntent().getStringExtra("userLastName");
        String userEmail = getIntent().getStringExtra("userEmail");
        String gender = getIntent().getStringExtra("gender");
        int id = getIntent().getIntExtra("id",-1);
        if (id == -1) {
            Log.e("TimerActivity", "ID is missing from Intent");
        }

        // Mostrar los datos en los TextViews e ImageView
        if (userName != null && userLastName != null && userEmail != null) {
            tvUserName.setText(userName);
            tvUserLastName.setText(userLastName);
            tvUserEmail.setText(userEmail);

            // Configuramos el ícono según el género
            if ("male".equalsIgnoreCase(gender)) {
                ivGender.setImageResource(R.drawable.ic_male); // Asegúrate de tener ic_male.png en res/drawable
            } else if ("female".equalsIgnoreCase(gender)) {
                ivGender.setImageResource(R.drawable.ic_female); // Asegúrate de tener ic_female.png en res/drawable
            }
        }

        // Inicializa el ViewModel
        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);

        // Observa los cambios en el temporizador
        timerViewModel.getTimeLeftMillis().observe(this, millis -> {
            int minutes = (int) (millis / 1000) / 60;
            int seconds = (int) (millis / 1000) % 60;
            String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
            tvTimer.setText(timeLeftFormatted);
        });

        // Observa si el temporizador está corriendo
        timerViewModel.isTimerRunning().observe(this, isRunning -> {
            btnPlay.setImageResource(isRunning ? R.drawable.ic_restart : R.drawable.ic_play);
        });


        // Configura el botón de inicio/reinicio
        btnPlay.setOnClickListener(view -> {
            if (Boolean.TRUE.equals(timerViewModel.isTimerRunning().getValue())) {
                timerViewModel.resetTimer();
            } else {
                //inicia el temporizador y muestra la lista de tareas
                timerViewModel.startTimer();
                Intent intent = new Intent(TimerActivity.this, TodoActivity.class);
                // Llama a fetchTodosFromApi con el userId
                if (id != -1) {
                    fetchTodosFromApi(id, userName);
                }
            }
        });
        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }
    private void logout() {
        getSharedPreferences("user_session", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        Intent intent = new Intent(TimerActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Finalizar la actividad actual
        finish();
    }

    private void fetchTodosFromApi(int userId, String username) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<TodoResponse> call = apiService.getUserTodos(userId);
        call.enqueue(new Callback<TodoResponse>() {
            @Override
            public void onResponse(Call<TodoResponse> call, Response<TodoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Todo> todos = response.body().getTodos(); // Almacena los todos
                    if (todos.isEmpty()) {
                        // Mostrar un MaterialAlertDialog diciendo que iniciará el tiempo de descanso
                        new MaterialAlertDialogBuilder(TimerActivity.this)
                                .setTitle("Tiempo de descanso")
                                .setMessage("No tienes tareas asignadas, se iniciará tu tiempo de descanso.")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    // Iniciar el temporizador de descanso
                                    timerViewModel.startTimer();
                                })
                                .show();
                    } else {
                        String todosJson = new Gson().toJson(todos);
                        Intent intent = new Intent(TimerActivity.this, TodoActivity.class);
                        intent.putExtra("todos", todosJson);
                        intent.putExtra("userName", username);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                } else {
                    Log.e("TimerActivity", "Error al obtener las tareas: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TodoResponse> call, Throwable t) {
                Log.e("TimerActivity", "Error de conexión", t);
            }
        });
    }

}
