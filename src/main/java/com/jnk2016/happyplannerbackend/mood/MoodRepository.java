package com.jnk2016.happyplannerbackend.mood;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoodRepository extends JpaRepository<Mood,Long> {
    List<Mood> findByUser(ApplicationUser user);
}
