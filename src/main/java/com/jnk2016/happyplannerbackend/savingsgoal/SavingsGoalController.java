package com.jnk2016.happyplannerbackend.savingsgoal;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/savings")
public class SavingsGoalController {
    private ApplicationUserRepository applicationUserRepository;
    private SavingsGoalRepository savingsGoalRepository;
    private Sort sort = Sort.by("deadline").ascending();

    public SavingsGoalController(ApplicationUserRepository applicationUserRepository, SavingsGoalRepository savingsGoalRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    /** Create a new savings goal for the logged in user */
    @PostMapping
    public HashMap<String, Object> newSavingsGoal(Authentication auth, @RequestBody SavingsGoal requestBody){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        requestBody.setUser(user);
        savingsGoalRepository.save(requestBody);
        return requestBody.toResponse();
    }

    @GetMapping
    public HashMap<String, Object> getAllSavingsGoals(Authentication auth) {
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        List<SavingsGoal> savingsRecords = savingsGoalRepository.findByUser(user, sort);
        HashMap<String, Object> response = new HashMap<>();
        List<HashMap<String, Object>> upcoming = new ArrayList<>();
        List<HashMap<String, Object>> passedDeadline = new ArrayList<>();

        for(SavingsGoal record : savingsRecords){
            HashMap<String, Object> recordResponse = record.toResponse();
            if((Boolean)recordResponse.get("passedDeadline")){
                upcoming.add(recordResponse);
            }
            else{
                passedDeadline.add(recordResponse);
            }
        }
        response.put("upcoming", upcoming);
        response.put("passed", passedDeadline);

        return response;
    }

    /** Create a new savings goal for the logged in user */
    @PutMapping("/{id}")
    public HashMap<String, Object> updateSavingsGoal (@PathVariable long id, @RequestBody SavingsGoal requestBody) throws Exception{
        SavingsGoal updatedEntry = savingsGoalRepository.findById(id).orElseThrow(()-> new Exception("None found!"));
        requestBody.setUser(updatedEntry.getUser());
        requestBody.setSavingsGoalId((updatedEntry.getSavingsGoalId()));
        savingsGoalRepository.save(requestBody);
        return requestBody.toResponse();
    }
}
