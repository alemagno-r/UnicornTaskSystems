package com.example.unicorntasksystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtPilot;
    private TextView txtActive;
    private TextView txtCompleted;
    private TextView txtTotal;
    private TextView txtProgress;
    private TextView txtSystemState;

    private ProgressBar progressSystem;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        txtPilot = findViewById(R.id.txtPilot);
        txtActive = findViewById(R.id.txtActive);
        txtCompleted = findViewById(R.id.txtCompleted);
        txtTotal = findViewById(R.id.txtTotal);
        txtProgress = findViewById(R.id.txtProgress);
        txtSystemState = findViewById(R.id.txtSystemState);
        progressSystem = findViewById(R.id.progressSystem);

        String username = getIntent().getStringExtra("username");

        if (username != null) {
            txtPilot.setText("PILOT: " + username.toUpperCase());
        }

        preferences = getSharedPreferences(
                "UNICORN_SYSTEM",
                MODE_PRIVATE
        );

        Button btnMissions = findViewById(R.id.btnMissions);
        Button btnNewMission = findViewById(R.id.btnNewMission);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnSettings = findViewById(R.id.btnSettings);

        btnMissions.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    TasksActivity.class
            );
            startActivity(intent);
        });

        btnNewMission.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    AddTaskActivity.class
            );
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (preferences != null) {
            updateSystemStatus();
        }
    }

    private void updateSystemStatus() {

        int total = 0;
        int completed = 0;
        int active = 0;

        try {

            String savedMissions =
                    preferences.getString("missions", "[]");

            JSONArray missions =
                    new JSONArray(savedMissions);

            total = missions.length();

            for (int i = 0; i < missions.length(); i++) {

                JSONObject mission =
                        missions.getJSONObject(i);

                boolean isCompleted =
                        mission.getBoolean("completed");

                if (isCompleted) {
                    completed++;
                } else {
                    active++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        int progress = 0;

        if (total > 0) {
            progress = (completed * 100) / total;
        }

        txtActive.setText(String.valueOf(active));
        txtCompleted.setText(String.valueOf(completed));
        txtTotal.setText(String.valueOf(total));

        progressSystem.setProgress(progress);

        txtProgress.setText(
                "SYSTEM READINESS: " + progress + "%"
        );

        if (progress >= 80) {

            txtSystemState.setText(
                    "● NT-D SYSTEM: OPTIMAL"
            );

            txtSystemState.setTextColor(
                    0xFF37B9FF
            );

        } else if (progress >= 50) {

            txtSystemState.setText(
                    "● NT-D SYSTEM: ACTIVE"
            );

            txtSystemState.setTextColor(
                    0xFF37B9FF
            );

        } else {

            txtSystemState.setText(
                    "● NT-D SYSTEM: LOW READINESS"
            );

            txtSystemState.setTextColor(
                    0xFFFF365C
            );
        }
    }
}