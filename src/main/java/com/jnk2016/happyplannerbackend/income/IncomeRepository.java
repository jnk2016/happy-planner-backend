package com.jnk2016.happyplannerbackend.income;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(ApplicationUser user);
    List<Income> findByUser(ApplicationUser user, Sort sort);
}
