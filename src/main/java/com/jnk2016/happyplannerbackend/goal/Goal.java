package com.jnk2016.happyplannerbackend.goal;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name="goal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="goal_id")
    private long goalId;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String steps;
    private String stepsCompleted ="";

    private int status;

    private LocalDateTime deadline;

    public Goal(ApplicationUser user, HashMap<String, Object> requestBody){
        this.name = (String)requestBody.get("name");
        this.steps = (String)requestBody.get("steps");
        String[] individualSteps = this.steps.split("•");
        for(int i = 1; i < individualSteps.length; i++){
            this.stepsCompleted += (i == individualSteps.length-1) ? "0" : "0,";
        }
        this.status = 0;
        LocalDate date = LocalDate.parse((String)requestBody.get("date"));
        LocalTime time = LocalTime.parse((String)requestBody.get("time"));
        this.deadline = date.atTime(time);
        this.user = user;
    }


    public HashMap<String, Object> toResponse(){
        HashMap<String, Object> response = new HashMap<>();
        List<HashMap<String,Object>> stepInfo = new ArrayList<>();

        response.put("id", this.goalId);
        response.put("name", this.name);
        response.put("status", this.status);
        response.put("deadline", this.deadline);

        String[] individualSteps = this.steps.split("•");
        String[] individualStepStatus = this.stepsCompleted.split(",");
        for(int i = 1; i < individualSteps.length; i++){
            HashMap<String,Object> newStepInfo = new HashMap<>();
            newStepInfo.put("details", individualSteps[i]);
            newStepInfo.put("completed", Integer.parseInt(individualStepStatus[i-1]));
            stepInfo.add(newStepInfo);
        }
        response.put("steps", stepInfo);

        return response;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
