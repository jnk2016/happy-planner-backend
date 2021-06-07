package com.jnk2016.happyplannerbackend.payment;

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
@Table(name="payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private long paymentId;
    private String name;
    private double amount;
    private int weeksUntilRepeat;
    private DayOfWeek dayRepeat;
    private LocalDate dueDate;
    private LocalDate nextDueDate;
    private LocalDate endDate;
    private Boolean paid = false;

    @Column(columnDefinition = "TEXT")
    private String note = "";

    public HashMap<String, Object> toResponse(){
        HashMap<String, Object> response = new HashMap<>();
        response.put("id", this.paymentId);
        response.put("name", this.name);
        response.put("amount", this.amount);
        response.put("weeks_until_repeat", this.weeksUntilRepeat);
        response.put("day_repeat", this.dayRepeat);
        response.put("due_date", this.dueDate);
        response.put("next_due_date", this.nextDueDate);
        response.put("end_date", this.endDate);
        response.put("note", this.note);
        response.put("paid", this.paid);

        return response;
    }
//    public Payment(ApplicationUser user, HashMap<String, Object> requestBody){
//
//    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
