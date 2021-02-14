package com.jnk2016.happyplannerbackend.cycle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CycleResponse {
    private long cycleId;
    private LocalDate recordedPeriodStart;
    private LocalDate predictedPeriodEnd;       // Prediction for when period will end
    private LocalDate recordedPeriodEnd;        // When period actually ended
    private LocalDate predictedNextPeriodStart; // When next cycle starts (also when the period starts)
    private LocalDate recordedCycleEnd;
    private long predictedCycleLength;

    private long daysUntilNextPeriod;
    private long cycleDay;

    public CycleResponse(Cycle cycle) {
        this.cycleId = cycle.getCycleId();
        this.recordedPeriodStart = cycle.getRecordedPeriodStart();
        this.predictedPeriodEnd = cycle.getPredictedPeriodEnd();
        this.recordedPeriodEnd = cycle.getRecordedPeriodEnd();
        this.predictedNextPeriodStart = cycle.getPredictedNextPeriodStart();
        this.recordedCycleEnd = cycle.getRecordedCycleEnd();
        this.predictedCycleLength = cycle.getPredictedCycleLength();

        /* Only for getting the current period */
        long untilPeriod = ChronoUnit.DAYS.between(LocalDate.now(), this.predictedNextPeriodStart);
        this.daysUntilNextPeriod = untilPeriod;
        long cycleDays = ChronoUnit.DAYS.between(this.recordedPeriodStart, LocalDate.now())+1;  // First Day = Day 1
        this.cycleDay = cycleDays;
    }
}
