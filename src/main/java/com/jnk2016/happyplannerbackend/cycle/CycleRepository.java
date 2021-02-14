package com.jnk2016.happyplannerbackend.cycle;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CycleRepository extends JpaRepository<Cycle,Long> {
    List<Cycle> findByUser(ApplicationUser user);
    Cycle findFirstByUser(ApplicationUser user, Sort sort);
}
