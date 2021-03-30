package com.jnk2016.happyplannerbackend.meal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealResponse {
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

    private boolean skipped;
    private String note;
    private LocalDate date;

    public MealResponse(Meal meal){
        this.mealId = meal.getMealId();
        this.name = meal.getName();
        this.dishes = meal.getDishes();
        this.type = meal.getType();

        this.servings = meal.getServings();
        this.calories = meal.getCalories();
        this.protein = meal.getProtein();
        this.fat = meal.getFat();
        this.carbs = meal.getCarbs();
        this.sugar = meal.getSugar();

        this.skipped = meal.isSkipped();
        this.note = meal.getNote();
        this.date = meal.getDate();
    }
}
