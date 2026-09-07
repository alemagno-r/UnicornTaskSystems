package com.example.unicorntasksystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;
    private final SharedPreferences preferences;

    public TaskAdapter(
            List<Task> taskList,
            SharedPreferences preferences
    ) {
        this.taskList = taskList;
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TaskViewHolder holder,
            int position
    ) {

        Task task = taskList.get(position);

        holder.txtTitle.setText(task.getTitle());

        holder.txtCategory.setText(
                "CATEGORY: " + task.getCategory()
        );

        holder.ratingBar.setRating(
                task.getPriority()
        );

        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setChecked(
                task.isCompleted()
        );

        updateMissionStatus(
                holder,
                task.isCompleted()
        );

        holder.checkBox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    int currentPosition =
                            holder.getAdapterPosition();

                    if (currentPosition != RecyclerView.NO_POSITION) {

                        task.setCompleted(isChecked);

                        saveCompletedState(
                                currentPosition,
                                isChecked
                        );

                        updateMissionStatus(
                                holder,
                                isChecked
                        );
                    }
                }
        );

        holder.itemView.setOnClickListener(v -> {

            int currentPosition =
                    holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {

                Intent intent = new Intent(
                        v.getContext(),
                        EditTaskActivity.class
                );

                intent.putExtra(
                        "mission_position",
                        currentPosition
                );

                v.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {

            int currentPosition =
                    holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {

                showDeleteDialog(
                        holder.itemView,
                        currentPosition
                );
            }

            return true;
        });
    }

    private void updateMissionStatus(
            TaskViewHolder holder,
            boolean completed
    ) {

        if (completed) {

            holder.txtMissionState.setText(
                    "● STATUS: COMPLETED"
            );

            holder.txtMissionState.setTextColor(
                    0xFF37B9FF
            );

        } else {

            holder.txtMissionState.setText(
                    "● STATUS: ACTIVE"
            );

            holder.txtMissionState.setTextColor(
                    0xFFFF365C
            );
        }
    }

    private void saveCompletedState(
            int position,
            boolean completed
    ) {

        if (position == RecyclerView.NO_POSITION) {
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

            if (position < missions.length()) {

                JSONObject mission =
                        missions.getJSONObject(position);

                mission.put(
                        "completed",
                        completed
                );

                preferences.edit()
                        .putString(
                                "missions",
                                missions.toString()
                        )
                        .apply();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteDialog(
            View view,
            int position
    ) {

        new AlertDialog.Builder(view.getContext())
                .setTitle("DELETE MISSION")
                .setMessage(
                        "¿Deseas eliminar esta misión?"
                )
                .setNegativeButton(
                        "CANCEL",
                        null
                )
                .setPositiveButton(
                        "DELETE",
                        (dialogInterface, which) -> {
                            deleteMission(position);
                        }
                )
                .show();
    }

    private void deleteMission(int position) {

        try {

            String savedMissions =
                    preferences.getString(
                            "missions",
                            "[]"
                    );

            JSONArray missions =
                    new JSONArray(savedMissions);

            if (position < missions.length()) {

                missions.remove(position);

                preferences.edit()
                        .putString(
                                "missions",
                                missions.toString()
                        )
                        .apply();

                taskList.remove(position);

                notifyItemRemoved(position);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtCategory;
        TextView txtMissionState;
        CheckBox checkBox;
        RatingBar ratingBar;

        public TaskViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtTitle =
                    itemView.findViewById(
                            R.id.txtTaskTitle
                    );

            txtCategory =
                    itemView.findViewById(
                            R.id.txtTaskCategory
                    );

            txtMissionState =
                    itemView.findViewById(
                            R.id.txtMissionState
                    );

            checkBox =
                    itemView.findViewById(
                            R.id.checkTask
                    );

            ratingBar =
                    itemView.findViewById(
                            R.id.ratingTask
                    );
        }
    }
}