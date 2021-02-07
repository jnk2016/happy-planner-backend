package com.jnk2016.happyplannerbackend.mood;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="mood")
@Data
public class Mood implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="mood_id")
    private long moodId;
    private String label;
    private String note = "";
    private LocalDate timestamp = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;
}
