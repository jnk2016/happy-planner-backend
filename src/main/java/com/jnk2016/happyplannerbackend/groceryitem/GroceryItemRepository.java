package com.jnk2016.happyplannerbackend.groceryitem;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroceryItemRepository extends JpaRepository<GroceryItem,Long> {
    List<GroceryItem> findByUser(ApplicationUser user);
}
