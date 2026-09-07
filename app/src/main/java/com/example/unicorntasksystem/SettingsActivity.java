package com.example.unicorntasksystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private TextView txtSystemMode;
    private TextView txtVersion;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        txtSystemMode = findViewById(R.id.txtSystemMode);
        txtVersion = findViewById(R.id.txtVersion);
        btnBack = findViewById(R.id.btnBack);

        txtSystemMode.setText("● SYSTEM MODE: ACTIVE");
        txtVersion.setText("UNICORN TASK SYSTEM\nVERSION 1.0");

        btnBack.setOnClickListener(v -> finish());
    }
}