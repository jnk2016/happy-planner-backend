package com.jnk2016.happyplannerbackend.meal;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="meal")
@Data
public class Meal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meal_id")
    private long mealId;
    private String name;
    private String dishes;
    private int type;   // 0 = breakfast, 1 = lunch, 2 = dinner

    private int servings;
    private double calories;
    private double protein;
    private double fat;
    private double carbs;
    private double sugar;

    private boolean skipped = false;
    @Column(columnDefinition = "TEXT")
    private String note;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
