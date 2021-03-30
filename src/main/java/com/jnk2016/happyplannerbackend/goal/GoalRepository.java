package com.jnk2016.happyplannerbackend.goal;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser(ApplicationUser user);
    List<Goal> findByUser(ApplicationUser user, Sort sort);
}
