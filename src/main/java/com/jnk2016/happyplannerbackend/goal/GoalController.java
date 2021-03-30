package com.jnk2016.happyplannerbackend.goal;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/goal")
public class GoalController {
    private ApplicationUserRepository applicationUserRepository;
    private GoalRepository goalRepository;
    private Sort sort = Sort.by("deadline").descending();

    public GoalController(ApplicationUserRepository applicationUserRepository, GoalRepository goalRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.goalRepository = goalRepository;
    }

    @PostMapping
    public void newGoal(Authentication auth, @RequestBody HashMap<String, Object> requestBody){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        Goal entry = new Goal(user, requestBody);
        goalRepository.save(entry);
    }

    @GetMapping
    public List<HashMap<String,Object>> getUpcomingGoals(Authentication auth){
        List<HashMap<String,Object>> response = new ArrayList<>();
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        List<Goal> goalRecords = goalRepository.findByUser(user, sort);
        LocalDate today = LocalDate.now();

        for(Goal record : goalRecords){
            if(record.getDeadline().toLocalDate().compareTo(today) < 0){    // Sorted by most future deadline date to least, if goal is not upcoming (past) then break
                break;
            }
            else{
                response.add(record.toResponse());
            }
        }
        return response;
    }

    @PutMapping("/{id}")
    public HashMap<String, Object> updateGoal(@RequestBody HashMap<String, Object> requestBody, @PathVariable long id) throws Exception{
        Goal goal = goalRepository.findById(id).orElseThrow(() -> new Exception("No such goal found!"));
        goal.setName((String)requestBody.get("name"));
        goal.setSteps((String)requestBody.get("steps"));
        goal.setStepsCompleted((String)requestBody.get("steps_completed"));

        LocalDate date = LocalDate.parse((String)requestBody.get("date"));
        LocalTime time = LocalTime.parse((String)requestBody.get("time"));
        goal.setDeadline(date.atTime(time));

        String[] stepDetails = goal.getSteps().split("•");
        String[] stepCompletion = goal.getStepsCompleted().split(",");
        double stepsCompleted = 0;
        double totalSteps = stepDetails.length;
        for(int i = 1; i < stepDetails.length; i++){
            if(stepCompletion[i-1].equals("1")){
                stepsCompleted++;
            }
        }
        goal.setStatus((int)((stepsCompleted/(totalSteps-1))*100));

        goalRepository.save(goal);
        return goal.toResponse();
    }
}
