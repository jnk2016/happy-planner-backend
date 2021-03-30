package com.jnk2016.happyplannerbackend.waterintake;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

@Entity
@Table(name="water_intake")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterIntake implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="water_intake_id")
    private long waterIntakeId;
    private double amount;
    private String notes;
    private LocalDateTime timestamp;

    public WaterIntake(ApplicationUser user, HashMap<String, Object> requestBody){
        this.amount = (double)requestBody.get("amount");
        this.notes = (String)requestBody.get("notes");
        LocalDate date = LocalDate.parse((String)requestBody.get("date"));
        LocalTime time = LocalTime.parse((String)requestBody.get("time"));
        this.timestamp = date.atTime(time);
        this.user = user;
    }

    public HashMap<String, Object> toResponse(){
        HashMap<String, Object> response = new HashMap<>();
        response.put("id", this.waterIntakeId);
        response.put("amount", this.amount);
        response.put("notes", this.notes);
        response.put("timestamp", this.timestamp);

        return response;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
