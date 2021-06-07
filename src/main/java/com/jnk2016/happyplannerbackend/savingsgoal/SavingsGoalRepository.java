package com.jnk2016.happyplannerbackend.savingsgoal;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    List<SavingsGoal> findByUser(ApplicationUser user);
    List<SavingsGoal> findByUser(ApplicationUser user, Sort sort);
}
