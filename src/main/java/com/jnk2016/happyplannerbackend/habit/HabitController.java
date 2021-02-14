package com.jnk2016.happyplannerbackend.habit;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/habits")
public class HabitController {
    private ApplicationUserRepository applicationUserRepository;
    private HabitRepository habitRepository;

    public HabitController(ApplicationUserRepository applicationUserRepository, HabitRepository habitRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.habitRepository = habitRepository;
    }

    /** Create a new habit */
    @PostMapping
    public void newHabit(Authentication auth, @RequestBody Habit habit){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));  /* Obtains the current user */
        habit.setUser(user);
        LocalDate currentDate = LocalDate.now();
        habit.setDate(currentDate);
        habitRepository.save(habit);
    }

    /** Get all of the habits pertaining to the logged in user */
    @GetMapping
    public List<HabitResponse> getHabits(Authentication auth){
        ApplicationUser user = applicationUserRepository.findByUsername(auth.getName());  /* Obtains the current user */
        List<Habit>userHabitData = habitRepository.findByUser(user);
        List<HabitResponse> habitResponse = new ArrayList<>();
        /* Iterate through habit list to map response object to each user habit */
        LocalDate currentDate = LocalDate.now();
        for(Habit userHabit : userHabitData){
            /* If there is a date change then update date, completed, streak, daily status, and streaks accordingly */
            if(!userHabit.getDate().equals(currentDate)){
                Period period = Period.between(currentDate, userHabit.getDate());
                if(userHabit.getCompleted() && period.getDays() == -1){
                    userHabit.setCompleted(false);
                    userHabit.setStreak(userHabit.getStreak()+1);
                    userHabit.setDailyStatus(0);
                }
                else if(userHabit.getDailyStatus()==-1){
                    userHabit.setDailyStatus(0);
                }
                else if(!userHabit.getCompleted() || period.getDays() < -1){
                    userHabit.setDailyStatus(0);
                    userHabit.setStreak(0);
                }
                userHabit.setDate(currentDate);
                habitRepository.save(userHabit);
            }
            /* Apply the HabitResponse class (so there's no infinite loop between getting user and habits) */
            HabitResponse habit = new HabitResponse(userHabit);
            habitResponse.add(habit);
        }
        return habitResponse;
    }

    /** Update a specific habit with new information */
    @PutMapping("/update/{id}")
    public void updateHabit(@PathVariable long id, @RequestBody Habit requestBody) throws Exception {
        Habit habit = habitRepository.findById(id).orElseThrow(()-> new Exception("This habit does not exist!"));
        habit.setLabel(requestBody.getLabel());
        habit.setDailyStatus(requestBody.getDailyStatus());
        habit.setDailyGoal(requestBody.getDailyGoal());
        habit.checkCompleted();
        habitRepository.save(habit);
    }

    /** Increment the daily status of a habit */
    @PutMapping("/{id}")
    public int incrementHabit(@PathVariable long id) throws Exception {
        Habit habit = habitRepository.findById(id).orElseThrow(()-> new Exception("This habit does not exist!"));
        int dailyStatus = habit.getDailyStatus();
        if (dailyStatus+1 > habit.getDailyGoal()) {   // If incrementing the habit is more than the daily goal, then return 0
            return 0;
        }
        else {
            habit.setDailyStatus(dailyStatus+1);
            habit.checkCompleted();
            habitRepository.save(habit);
            return 1;
        }
    }

    /** Delete a habit */
    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable long id) throws Exception {
        Habit habit = habitRepository.findById(id).orElseThrow(()-> new Exception("This habit does not exist!"));
        habitRepository.delete(habit);
    }
}
