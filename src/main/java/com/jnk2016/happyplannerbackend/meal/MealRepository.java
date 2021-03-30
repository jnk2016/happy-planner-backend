package com.jnk2016.happyplannerbackend.meal;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUser(ApplicationUser user);
    List<Meal> findByUser(ApplicationUser user, Sort sort);
}
