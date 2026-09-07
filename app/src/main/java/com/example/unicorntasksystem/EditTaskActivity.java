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

public class EditTaskActivity extends AppCompatActivity {

    private EditText editMission;
    private Spinner spinnerCategory;
    private RadioGroup radioType;
    private RatingBar ratingMission;
    private Button btnSaveMission;

    private SharedPreferences preferences;

    private int missionPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_task);

        editMission = findViewById(R.id.editMission);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioType = findViewById(R.id.radioType);
        ratingMission = findViewById(R.id.ratingMission);
        btnSaveMission = findViewById(R.id.btnSaveMission);

        preferences = getSharedPreferences(
                "UNICORN_SYSTEM",
                MODE_PRIVATE
        );

        missionPosition = getIntent().getIntExtra(
                "mission_position",
                -1
        );

        setupCategorySpinner();
        loadMission();

        btnSaveMission.setOnClickListener(v -> updateMission());
    }

    private void setupCategorySpinner() {

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
    }

    private void loadMission() {

        if (missionPosition == -1) {
            Toast.makeText(
                    this,
                    "Misión no encontrada",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        try {

            String savedMissions =
                    preferences.getString(
                            "missions",
                            "[]"
                    );

            JSONArray missions =
                    new JSONArray(savedMissions);

            if (missionPosition >= missions.length()) {
                finish();
                return;
            }

            JSONObject mission =
                    missions.getJSONObject(missionPosition);

            editMission.setText(
                    mission.getString("title")
            );

            String category =
                    mission.getString("category");

            setCategory(category);

            String type =
                    mission.getString("type");

            if (type.equals("PRIMARY MISSION")) {

                RadioButton primary =
                        findViewById(R.id.radioPrimary);

                primary.setChecked(true);

            } else {

                RadioButton secondary =
                        findViewById(R.id.radioSecondary);

                secondary.setChecked(true);
            }

            int priority =
                    mission.getInt("priority");

            ratingMission.setRating(priority);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error al cargar la misión",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        }
    }

    private void setCategory(String category) {

        for (int i = 0;
             i < spinnerCategory.getCount();
             i++) {

            if (spinnerCategory
                    .getItemAtPosition(i)
                    .toString()
                    .equals(category)) {

                spinnerCategory.setSelection(i);
                break;
            }
        }
    }

    private void updateMission() {

        String missionName =
                editMission
                        .getText()
                        .toString()
                        .trim();

        int selectedRadio =
                radioType.getCheckedRadioButtonId();

        if (missionName.isEmpty()) {

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

        try {

            String savedMissions =
                    preferences.getString(
                            "missions",
                            "[]"
                    );

            JSONArray missions =
                    new JSONArray(savedMissions);

            if (missionPosition >= missions.length()) {
                return;
            }

            JSONObject mission =
                    missions.getJSONObject(missionPosition);

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

            mission.put(
                    "title",
                    missionName
            );

            mission.put(
                    "category",
                    category
            );

            mission.put(
                    "type",
                    type
            );

            mission.put(
                    "priority",
                    priority
            );

            preferences.edit()
                    .putString(
                            "missions",
                            missions.toString()
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "MISSION UPDATED",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error al actualizar la misión",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}