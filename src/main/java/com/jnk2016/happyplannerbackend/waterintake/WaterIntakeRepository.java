package com.jnk2016.happyplannerbackend.waterintake;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {
    List<WaterIntake> findByUser(ApplicationUser user);
    List<WaterIntake> findByUser(ApplicationUser user, Sort sort);
}
