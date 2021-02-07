package com.jnk2016.happyplannerbackend.habit;

import lombok.Data;

@Data
public class HabitResponse {
    private long habitId;
    private String label;
    private int dailyGoal;
    private int dailyStatus;
    private boolean completed;
    private int streak;

    public HabitResponse(long habitId, String label, int dailyGoal, int dailyStatus, boolean completed, int streak) {
        this.habitId = habitId;
        this.label = label;
        this.dailyGoal = dailyGoal;
        this.dailyStatus = dailyStatus;
        this.completed = completed;
        this.streak = streak;
    }
}
