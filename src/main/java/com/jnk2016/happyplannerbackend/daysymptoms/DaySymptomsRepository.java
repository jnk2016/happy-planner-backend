package com.jnk2016.happyplannerbackend.daysymptoms;

import com.jnk2016.happyplannerbackend.cycle.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaySymptomsRepository extends JpaRepository<DaySymptoms,Long> {
    List<DaySymptoms> findByCycle(Cycle cycle);
}
