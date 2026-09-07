package com.example.unicorntasksystem;

public class Task {

    private String title;
    private String category;
    private int priority;
    private boolean completed;

    public Task(
            String title,
            String category,
            int priority,
            boolean completed) {

        this.title = title;
        this.category = category;
        this.priority = priority;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}