package com.jnk2016.happyplannerbackend.cycle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CycleRequest {
    Boolean firstCycle;
    LocalDate recentPeriodStart;
    LocalDate recentPeriodEnd;   // If 1st cycle, recentPeriodEnd = recentPeriodStart.plusDays(previousPeriodLength); else, recentPeriodEnd = null
    long previousCycleLength;
    long previousPeriodLength;
}
