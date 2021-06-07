package com.jnk2016.happyplannerbackend.income;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;

@Entity
@Table(name="income")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Income implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="income_id")
    private long incomeId;
    private String name;
    private double amount;
    private int weeksUntilRepeat;
    private DayOfWeek dayRepeat;
    private LocalDate dateReceived;
    private LocalDate nextDateReceived;
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String note = "";

    public HashMap<String, Object> toResponse(){
        HashMap<String, Object> response = new HashMap<>();
        response.put("id", this.incomeId);
        response.put("name", this.name);
        response.put("amount", this.amount);
        response.put("weeks_until_repeat", this.weeksUntilRepeat);
        response.put("day_repeat", this.dayRepeat);
        response.put("date_received", this.dateReceived);
        response.put("next_date_received", this.nextDateReceived);
        response.put("end_date", this.endDate);
        response.put("note", this.note);

        return response;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
