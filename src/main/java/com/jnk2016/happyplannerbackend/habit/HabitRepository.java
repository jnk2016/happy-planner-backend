package com.jnk2016.happyplannerbackend.habit;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUser(ApplicationUser user);
}