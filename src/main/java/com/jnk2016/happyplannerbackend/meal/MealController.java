package com.jnk2016.happyplannerbackend.meal;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/meals")
public class MealController {
    private ApplicationUserRepository applicationUserRepository;
    private MealRepository mealRepository;
    private Sort sort = Sort.by("date").descending();

    public MealController(ApplicationUserRepository applicationUserRepository, MealRepository mealRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.mealRepository = mealRepository;
    }

    /** Create a new meal for the logged in user */
    @PostMapping
    public void newMeal(Authentication auth, @RequestBody Meal meal){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        meal.setUser(user);
        mealRepository.save(meal);
    }

    /** Update attributes of a meal */
    @PutMapping("/{id}")
    public void updateMeal(@PathVariable long id, @RequestBody Meal requestBody) throws Exception {
        Meal meal = mealRepository.findById(id).orElseThrow(() -> new Exception("This meal does not exist!"));
        mealRepository.save(meal);
    }

    /** Delete a meal */
    @DeleteMapping("/{id}")
    public void deleteMeal(@PathVariable long id) throws Exception {
        Meal meal = mealRepository.findById(id).orElseThrow(() -> new Exception("This meal does not exist!"));
        mealRepository.delete(meal);
    }

    /** Get all the meals for a day specified */
    @GetMapping("/day")
    public HashMap<String,Object> getMealsOfDay(Authentication auth, @RequestBody Meal requestBody) {
        HashMap<String,Object> response = new HashMap<>();
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        List<Meal> meals = mealRepository.findByUser(user, sort);
        List<MealResponse> responseMeals = new ArrayList<>();
        int dayMealCount = 0;
        double dayCalories = 0;
        for(Meal meal : meals){
            if(meal.getDate().compareTo(requestBody.getDate()) == 0){
                MealResponse dayMeal = new MealResponse(meal);
                responseMeals.add(dayMeal);
                dayCalories += meal.getCalories();
                dayMealCount++;
            }
            if(dayMealCount == 3 || meal.getDate().compareTo(requestBody.getDate()) < 0) { break; }
        }
        response.put("total_cal_today", dayCalories);
        response.put("meals", responseMeals);
        return response;
    }
}
