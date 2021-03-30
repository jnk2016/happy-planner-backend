package com.jnk2016.happyplannerbackend.user;

import com.jnk2016.happyplannerbackend.braindumpnote.BrainDumpNote;
import com.jnk2016.happyplannerbackend.cycle.Cycle;
import com.jnk2016.happyplannerbackend.goal.Goal;
import com.jnk2016.happyplannerbackend.groceryitem.GroceryItem;
import com.jnk2016.happyplannerbackend.habit.Habit;
import com.jnk2016.happyplannerbackend.meal.Meal;
import com.jnk2016.happyplannerbackend.mood.Mood;
import com.jnk2016.happyplannerbackend.recipe.Recipe;
import com.jnk2016.happyplannerbackend.task.Task;
import com.jnk2016.happyplannerbackend.waterintake.WaterIntake;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="user")
@Data
public class ApplicationUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private long userId;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private double dailyWater;

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Habit> habits;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mood> moods;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BrainDumpNote> brainDumpNotes;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cycle> cycles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recipe> recipes;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GroceryItem> groceries;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Meal> meals;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WaterIntake> waterIntake;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Goal> goals;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;
}