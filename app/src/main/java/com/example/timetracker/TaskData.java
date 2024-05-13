package com.example.timetracker;

public class TaskData {
    private int id;
    private String typeActivitySelected;
    private String taskName;
    private String selectedStartTime;
    private String selectedEndTime;
    private String difficultyColor;

    public TaskData() {
        // Constructeur vide
    }

    // Autres constructeurs et méthodes getters/setters...

    public String getTypeActivitySelected() {
        return typeActivitySelected;
    }

    public void setTypeActivitySelected(String typeActivitySelected) {
        this.typeActivitySelected = typeActivitySelected;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSelectedStartTime() {
        return selectedStartTime;
    }

    public void setSelectedStartTime(String selectedStartTime) {
        this.selectedStartTime = selectedStartTime;
    }

    public String getSelectedEndTime() {
        return selectedEndTime;
    }

    public void setSelectedEndTime(String selectedEndTime) {
        this.selectedEndTime = selectedEndTime;
    }

    public String getDifficultyColor() {
        return difficultyColor;
    }

    public void setDifficultyColor(String difficultyColor) {
        this.difficultyColor = difficultyColor;
    }
}
