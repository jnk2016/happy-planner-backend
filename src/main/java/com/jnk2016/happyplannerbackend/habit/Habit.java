package com.jnk2016.happyplannerbackend.habit;
import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="habit")
@Data
public class Habit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="habit_id")
    private long habitId;
    private String label;
    private int dailyGoal;
    private int dailyStatus = 0;
    private boolean completed = false;
    private int streak = 0;
    private LocalDate date = LocalDate.now();

    public boolean getCompleted(){
        return completed;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
