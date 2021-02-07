package com.jnk2016.happyplannerbackend.habit;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/habits")
public class HabitController {
    private ApplicationUserRepository applicationUserRepository;
    private HabitRepository habitRepository;

    public HabitController(ApplicationUserRepository applicationUserRepository,
                          HabitRepository habitRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.habitRepository = habitRepository;
    }


    @PostMapping("/new")
    public void newHabit(Authentication auth, @RequestBody Habit habit){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));  /** Obtains the current user */
        habit.setUser(user);
        habitRepository.save(habit);
    }

    @PostMapping("/{id}")
    public void updateHabit(@PathVariable long id, @RequestBody Habit updatedHabit){
        Optional<Habit> oldHabit = habitRepository.findById(id);
        oldHabit.get().setLabel(updatedHabit.getLabel());
        oldHabit.get().setDailyStatus(updatedHabit.getDailyStatus());
        oldHabit.get().setDailyGoal(updatedHabit.getDailyGoal());
        habitRepository.save(oldHabit.get());
    }

    @GetMapping("/user")
    public List<HabitResponse> getHabits(Authentication auth){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));  /** Obtains the current user */
        List<Habit>userHabitData = habitRepository.findByUser(user);
        List<HabitResponse> habitResponse = new ArrayList<>();
        /* Iterate through habit list to map response object to each user habit */
        for(Habit userHabit : userHabitData){
            /* If there is a date change then update date and streaks */
            if(userHabit.getDate() != LocalDate.now()){
                Period period = Period.between(LocalDate.now(), userHabit.getDate());
                if(userHabit.getCompleted() && period.getDays() == -1){
                    userHabit.setCompleted(false);
                    userHabit.setStreak(userHabit.getStreak()+1);
                }
                else if(userHabit.getDailyStatus()==-1){
                    userHabit.setDailyStatus(0);
                }
                else if(userHabit.getCompleted() == false || period.getDays() < -1){
                    userHabit.setStreak(0);
                }
                userHabit.setDate(LocalDate.now());
                habitRepository.save(userHabit);
            }
            HabitResponse habit = new HabitResponse(userHabit.getHabitId(),userHabit.getLabel(), userHabit.getDailyGoal(), userHabit.getDailyStatus(), userHabit.getCompleted(), userHabit.getStreak());
            habitResponse.add(habit);
        }
        return habitResponse;
    }

    /* Increment the daily status of a habit */
    @PutMapping("/{id}")
    public int incrementHabit(@PathVariable long id){
        Optional<Habit> habit = habitRepository.findById(id);
        int dailyStatus = habit.get().getDailyStatus();
        if (dailyStatus+1 > habit.get().getDailyGoal()) {   // If incrementing the habit is more than the daily goal, then return 0
            return 0;
        }
        else {
            habit.get().setDailyStatus(dailyStatus+1);
            if(dailyStatus+1 == habit.get().getDailyGoal()){    // If the daily status meets the daily goal then set to completed (for the day)
                habit.get().setCompleted(true);
            }
            habitRepository.save(habit.get());
            return 1;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable long id) throws Exception {
        Habit habit = habitRepository.findById(id).orElseThrow(()-> new Exception("This habit does not exist!"));
        habitRepository.delete(habit);
    }
}
