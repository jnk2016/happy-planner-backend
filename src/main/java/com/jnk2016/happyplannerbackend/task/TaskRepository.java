package com.jnk2016.happyplannerbackend.task;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(ApplicationUser user);
    List<Task> findByUser(ApplicationUser user, Sort sort);
}
