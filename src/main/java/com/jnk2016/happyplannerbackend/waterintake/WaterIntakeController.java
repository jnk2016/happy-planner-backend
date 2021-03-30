package com.jnk2016.happyplannerbackend.waterintake;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/water")
public class WaterIntakeController {
    private ApplicationUserRepository applicationUserRepository;
    private WaterIntakeRepository waterIntakeRepository;
    private Sort sort = Sort.by("timestamp").descending();

    public WaterIntakeController(ApplicationUserRepository applicationUserRepository, WaterIntakeRepository waterIntakeRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.waterIntakeRepository = waterIntakeRepository;
    }

    /** Create a new water intake for the logged in user */
    @PostMapping
    public void newWaterIntake(Authentication auth, @RequestBody HashMap<String, Object> requestBody){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        WaterIntake entry = new WaterIntake(user, requestBody);
        waterIntakeRepository.save(entry);
    }

    @GetMapping
    public HashMap<String, Object> getDayActivity(Authentication auth, @RequestBody HashMap<String, LocalDate> requestBody) {
        HashMap<String, Object> activity = new HashMap<>();
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        List<WaterIntake> userWaterRecords = waterIntakeRepository.findByUser(user, sort);
        LocalDate today = requestBody.containsKey("date") ? requestBody.get("date") : LocalDate.now();
        List<HashMap<String, Object>> dailyData = new ArrayList<>();

        double intake = 0;
        int cupsCompleted = 0;
        for(WaterIntake record : userWaterRecords){
            if(record.getTimestamp().toLocalDate().compareTo(today) == 0){
                intake += record.getAmount();

                HashMap<String, Object> recordData = record.toResponse();
                dailyData.add(recordData);
            }
            else if(record.getTimestamp().toLocalDate().compareTo(today) < 0){
                break;
            }
        }
        int cupsGoal = (int)(user.getDailyWater()/236.588);
        cupsCompleted = (int)(intake/236.588);
        int percent = (int)((intake/ user.getDailyWater())*100);

        activity.put("daily_goal_ml", user.getDailyWater());
        activity.put("daily_goal_cups", cupsGoal);
        activity.put("intake_ml", intake);
        activity.put("intake_cups", cupsCompleted);
        activity.put("percent", percent);
        activity.put("items", dailyData);

        return activity;
    }

    @GetMapping("/weekly")
    public HashMap<String, Object> getWeeklyIntake(Authentication auth){
        HashMap<String, Object> response = new HashMap<>();
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        List<WaterIntake> userWaterRecords = waterIntakeRepository.findByUser(user, sort);

        // Get the most recent sunday
        LocalDate recentSun = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        List<LocalDate> dateRange = new ArrayList<>();
        dateRange.add(recentSun);
        for(int i = 0; i < 6; i++){ // Add dates corresponding to days of the week following the most previous sunday
            dateRange.add(recentSun.plusDays(i+1));
        }

        HashMap<String, Object> weeklyData = new HashMap<>();
        double total = 0;
        for(WaterIntake record : userWaterRecords){ // Iterate through records ordered by most recent
            LocalDate recordDate = record.getTimestamp().toLocalDate();
            if(recordDate.compareTo(recentSun) < 0){    // If date is less than entry then end for loop
                break;
            }
            else{   // Otherwise, iterate through days of week to add matching date's total water intake
                for(int i = 0; i < 7; i++){
                    if(dateRange.get(i).compareTo(recordDate) == 0){
                        total += record.getAmount();
                        if(weeklyData.containsKey(recordDate.getDayOfWeek().toString())){   // If already an entry, add to total of that specific date
                            double amount = (double)weeklyData.get(recordDate.getDayOfWeek().toString());
                            weeklyData.put(recordDate.getDayOfWeek().toString(), amount+record.getAmount());    // Json body is dayofweek(key):totaldayintake(val in ml)
                        }
                        else {  // If no entry in this day of week yet, create new entry using record amount as value
                            weeklyData.put(recordDate.getDayOfWeek().toString(), record.getAmount());
                        }
                        break;  // break the loop is an entry is found
                    }
                }
            }
        }

        // Build json body
        response.put("items", weeklyData);
        response.put("daily_average", total/weeklyData.size());
        response.put("total", total);
        response.put("goal", user.getDailyWater());

        return response;
    }

    @GetMapping("/streak")
    public HashMap<String, Integer> getStreak(Authentication auth){
        HashMap<String, Integer> response = new HashMap<>();
        LocalDate date = LocalDate.now();
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        List<WaterIntake> userWaterRecords = waterIntakeRepository.findByUser(user, sort);
        int currentStreak = 0;

        for(WaterIntake record: userWaterRecords){
            if(record.getTimestamp().toLocalDate().compareTo(date) == 0){
                currentStreak++;
                date = date.minusDays(1);
            }
            else if(record.getTimestamp().toLocalDate().compareTo(date) < 0){
                break;
            }
        }
        response.put("streak", currentStreak);

        return response;
    }

}
