package com.jnk2016.happyplannerbackend.recipe;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long>{
    List<Recipe> findByUser(ApplicationUser user, Sort sort);
}
