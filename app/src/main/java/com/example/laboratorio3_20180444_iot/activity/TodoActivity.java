package com.example.laboratorio3_20180444_iot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laboratorio3_20180444_iot.R;
import com.example.laboratorio3_20180444_iot.activity.login.LoginActivity;
import com.example.laboratorio3_20180444_iot.activity.timer.TimerActivity;
import com.example.laboratorio3_20180444_iot.api.ApiClient;
import com.example.laboratorio3_20180444_iot.api.ApiService;
import com.example.laboratorio3_20180444_iot.models.Todo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoActivity extends AppCompatActivity {
    private Spinner spinnerTodos;
    private Button btnChangeStatus;
    private ImageButton btnBack;
    private ImageButton btnLogout;
    private List<Todo> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        TextView tvUserTasks = findViewById(R.id.tvUserTasks);
        spinnerTodos = findViewById(R.id.spinnerTodos);
        btnChangeStatus = findViewById(R.id.btnChangeStatus);
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);

        String userName = getIntent().getStringExtra("userName");
        tvUserTasks.setText("Ver tareas de " + userName);

        String todosJson = getIntent().getStringExtra("todos");

        if (todosJson != null && !todosJson.isEmpty()) {
            Type listType = new TypeToken<List<Todo>>() {}.getType();
            List<Todo> todos = new Gson().fromJson(todosJson, listType);

            ArrayAdapter<Todo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, todos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTodos.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No hay tareas disponibles", Toast.LENGTH_SHORT).show();
        }

        btnChangeStatus.setOnClickListener(v -> changeTodoStatus((Todo) spinnerTodos.getSelectedItem()));
        btnBack.setOnClickListener(v -> finish());
        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        getSharedPreferences("user_session", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        Intent intent = new Intent(TodoActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Finalizar la actividad actual
        finish();
    }

    private void changeTodoStatus(Todo todo) {
        if (todo != null) {
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            todo.setCompleted(!todo.isCompleted());

            Call<Todo> call = apiService.updateTodo(todo.getId(), todo);
            call.enqueue(new retrofit2.Callback<Todo>() {
                @Override
                public void onResponse(Call<Todo> call, retrofit2.Response<Todo> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(TodoActivity.this, "Estado actualizado correctamente", Toast.LENGTH_SHORT).show();

                        todo.setCompleted(!todo.isCompleted());
                        ((ArrayAdapter) spinnerTodos.getAdapter()).notifyDataSetChanged();
                    } else {
                        Toast.makeText(TodoActivity.this, "Error al actualizar el estado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Todo> call, Throwable t) {
                    Toast.makeText(TodoActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(TodoActivity.this, "Selecciona una tarea", Toast.LENGTH_SHORT).show();
        }
    }

}
