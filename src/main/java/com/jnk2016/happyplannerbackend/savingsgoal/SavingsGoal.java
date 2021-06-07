package com.jnk2016.happyplannerbackend.savingsgoal;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

@Entity
@Table(name="savings_goal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingsGoal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="savings_goal_id")
    private long savingsGoalId;
    private String name;
    private long saved;
    private long goal;
    private LocalDate deadline;


    @Column(columnDefinition = "TEXT")
    private String note = "";

    public HashMap<String, Object> toResponse(){
        HashMap<String, Object> response = new HashMap<>();
        response.put("id", this.savingsGoalId);
        response.put("name", this.name);
        response.put("saved", this.saved);
        response.put("goal", this.goal);
        response.put("deadline", this.deadline);
        Boolean pastDeadline = this.deadline.compareTo(LocalDate.now()) < 0;
        response.put("past_deadline", pastDeadline);

        return response;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
