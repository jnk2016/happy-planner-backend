package com.jnk2016.happyplannerbackend.cycle;

import com.jnk2016.happyplannerbackend.daysymptoms.DaySymptoms;
import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="cycle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cycle implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cycle_id")
    private long cycleId;
    private LocalDate recordedPeriodStart;      // Start of cycle (start of period)
    private LocalDate predictedPeriodEnd;       // Prediction for when period will end
    private LocalDate recordedPeriodEnd;        // When period actually ended
    private LocalDate predictedNextPeriodStart; // When next cycle starts (also when the next period starts)
    private LocalDate recordedCycleEnd;
    private long predictedCycleLength;

    // Cycle constructor with calculations for initial period
    public Cycle(LocalDate recentPeriodStart, LocalDate periodEnd, long previousCycleLength, long previousPeriodLength) {
        this.recordedPeriodStart = recentPeriodStart;
        this.predictedPeriodEnd = recentPeriodStart.plusDays(previousPeriodLength);
        this.recordedPeriodEnd = periodEnd;
        this.predictedNextPeriodStart = recentPeriodStart.plusDays(previousCycleLength);
        this.predictedCycleLength = previousCycleLength;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private ApplicationUser user;

    @OneToMany(mappedBy = "cycle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DaySymptoms> daySymptoms;
}
