package com.example.unicorntasksystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private RecyclerView recyclerTasks;

    private TaskAdapter taskAdapter;

    private List<Task> taskList;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tasks);

        recyclerTasks =
                findViewById(R.id.recyclerTasks);

        recyclerTasks.setLayoutManager(
                new LinearLayoutManager(this)
        );

        preferences =
                getSharedPreferences(
                        "UNICORN_SYSTEM",
                        MODE_PRIVATE
                );

        loadMissions();

        Button btnAddMission =
                findViewById(R.id.btnAddMission);

        btnAddMission.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            TasksActivity.this,
                            AddTaskActivity.class
                    );

            startActivity(intent);
        });
    }

    private void loadMissions() {

        taskList = new ArrayList<>();

        String savedMissions =
                preferences.getString(
                        "missions",
                        "[]"
                );

        try {

            JSONArray missions =
                    new JSONArray(savedMissions);

            if (missions.length() == 0) {

                createDefaultMissions();

                savedMissions =
                        preferences.getString(
                                "missions",
                                "[]"
                        );

                missions =
                        new JSONArray(savedMissions);
            }

            for (int i = 0;
                 i < missions.length();
                 i++) {

                JSONObject mission =
                        missions.getJSONObject(i);

                String title =
                        mission.getString("title");

                String category =
                        mission.getString("category");

                int priority =
                        mission.getInt("priority");

                boolean completed =
                        mission.getBoolean("completed");

                taskList.add(
                        new Task(
                                title,
                                category,
                                priority,
                                completed
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        taskAdapter =
                new TaskAdapter(
                        taskList,
                        preferences
                );

        recyclerTasks.setAdapter(
                taskAdapter
        );
    }

    private void createDefaultMissions() {

        try {

            JSONArray missions =
                    new JSONArray();

            JSONObject mission1 =
                    new JSONObject();

            mission1.put(
                    "title",
                    "Calibración de sensores"
            );

            mission1.put(
                    "category",
                    "SYSTEM"
            );

            mission1.put(
                    "type",
                    "PRIMARY MISSION"
            );

            mission1.put(
                    "priority",
                    5
            );

            mission1.put(
                    "completed",
                    false
            );

            missions.put(mission1);

            JSONObject mission2 =
                    new JSONObject();

            mission2.put(
                    "title",
                    "Entrenamiento de piloto"
            );

            mission2.put(
                    "category",
                    "TRAINING"
            );

            mission2.put(
                    "type",
                    "PRIMARY MISSION"
            );

            mission2.put(
                    "priority",
                    4
            );

            mission2.put(
                    "completed",
                    false
            );

            missions.put(mission2);

            JSONObject mission3 =
                    new JSONObject();

            mission3.put(
                    "title",
                    "Mantenimiento del RX-0"
            );

            mission3.put(
                    "category",
                    "MAINTENANCE"
            );

            mission3.put(
                    "type",
                    "SECONDARY MISSION"
            );

            mission3.put(
                    "priority",
                    5
            );

            mission3.put(
                    "completed",
                    true
            );

            missions.put(mission3);

            preferences.edit()
                    .putString(
                            "missions",
                            missions.toString()
                    )
                    .apply();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (preferences != null) {

            loadMissions();
        }
    }
}