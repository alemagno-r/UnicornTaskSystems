package com.example.unicorntasksystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editMission;
    private Spinner spinnerCategory;
    private RadioGroup radioType;
    private RatingBar ratingMission;
    private Button btnSaveMission;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);

        editMission = findViewById(R.id.editMission);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioType = findViewById(R.id.radioType);
        ratingMission = findViewById(R.id.ratingMission);
        btnSaveMission = findViewById(R.id.btnSaveMission);

        preferences = getSharedPreferences(
                "UNICORN_SYSTEM",
                MODE_PRIVATE
        );

        String[] categories = {
                "SYSTEM",
                "TRAINING",
                "MAINTENANCE",
                "COMBAT"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(adapter);

        btnSaveMission.setOnClickListener(v -> registerMission());
    }

    private void registerMission() {

        String mission = editMission
                .getText()
                .toString()
                .trim();

        int selectedRadio =
                radioType.getCheckedRadioButtonId();

        if (mission.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingresa un nombre para la misión",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (selectedRadio == -1) {

            Toast.makeText(
                    this,
                    "Selecciona el tipo de misión",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        RadioButton selectedType =
                findViewById(selectedRadio);

        String category =
                spinnerCategory
                        .getSelectedItem()
                        .toString();

        String type =
                selectedType
                        .getText()
                        .toString();

        int priority =
                (int) ratingMission.getRating();

        try {

            String savedMissions =
                    preferences.getString(
                            "missions",
                            "[]"
                    );

            JSONArray missions =
                    new JSONArray(savedMissions);

            JSONObject newMission =
                    new JSONObject();

            newMission.put(
                    "title",
                    mission
            );

            newMission.put(
                    "category",
                    category
            );

            newMission.put(
                    "type",
                    type
            );

            newMission.put(
                    "priority",
                    priority
            );

            newMission.put(
                    "completed",
                    false
            );

            missions.put(newMission);

            preferences.edit()
                    .putString(
                            "missions",
                            missions.toString()
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "MISSION REGISTERED",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error al guardar la misión",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}