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

    public HabitResponse(Habit habit) {
        this.habitId = habit.getHabitId();
        this.label = habit.getLabel();
        this.dailyGoal = habit.getDailyGoal();
        this.dailyStatus = habit.getDailyStatus();
        this.completed = habit.getCompleted();
        this.streak = habit.getStreak();
    }
}
