package com.jnk2016.happyplannerbackend.groceryitem;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/groceries")
public class GroceryItemController {
    private ApplicationUserRepository applicationUserRepository;
    private GroceryItemRepository groceryItemRepository;

    public GroceryItemController(ApplicationUserRepository applicationUserRepository, GroceryItemRepository groceryItemRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.groceryItemRepository = groceryItemRepository;
    }

    /** Create a new grocery item for the logged in user */
    @PostMapping
    public void newGroceryItem(Authentication auth, @RequestBody GroceryItem groceryItem){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        groceryItem.setUser(user);
        groceryItemRepository.save(groceryItem);
    }

    /** Get all of a user's grocery items within each category */
    @GetMapping
    public HashMap<String, List<GroceryItemResponse>> getGroceries(Authentication auth){
        HashMap<String, List<GroceryItemResponse>> response = new HashMap<>();
        ApplicationUser user = applicationUserRepository.findByUsername(auth.getName());
        List<GroceryItem>userGroceryItemData = groceryItemRepository.findByUser(user);
        for(GroceryItem userGroceryItem : userGroceryItemData){
            String key = userGroceryItem.getType() == 0 ? "Produce" : userGroceryItem.getType() == 1 ? "Meat" :
                        userGroceryItem.getType() == 2 ? "Pantry" : userGroceryItem.getType() == 3 ? "Frozen" :
                        userGroceryItem.getType() == 4 ? "Drinks" :  "Bakery";
            List<GroceryItemResponse> groceryItemsOfType = (!response.containsKey(key) ? new ArrayList<>() : response.get(key));
            GroceryItemResponse groceryItem = new GroceryItemResponse(userGroceryItem);
            groceryItemsOfType.add(groceryItem);
            response.put(key, groceryItemsOfType);
        }
        return response;
    }

    /** Update a grocery item */
    @PutMapping("/{id}")
    public void updateGroceryItem(@PathVariable long id, @RequestBody GroceryItem requestBody) throws Exception {
        GroceryItem groceryItem = groceryItemRepository.findById(id).orElseThrow(() -> new Exception("This item does not exist!"));
        groceryItem.setName(requestBody.getName());
        groceryItemRepository.save(groceryItem);
    }

    /** Delete a grocery item */
    @DeleteMapping("/{id}")
    public void deleteGroceryItem(@PathVariable long id) throws Exception {
        GroceryItem groceryItem = groceryItemRepository.findById(id).orElseThrow(() -> new Exception("This mood does not exist!"));
        groceryItemRepository.delete(groceryItem);
    }
}
