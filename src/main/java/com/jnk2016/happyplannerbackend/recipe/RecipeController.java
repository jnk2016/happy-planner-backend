package com.jnk2016.happyplannerbackend.recipe;

import com.jnk2016.happyplannerbackend.user.ApplicationUser;
import com.jnk2016.happyplannerbackend.user.ApplicationUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private ApplicationUserRepository applicationUserRepository;
    private RecipeRepository recipeRepository;
    private Sort sort = Sort.by("type").ascending();

    public RecipeController(ApplicationUserRepository applicationUserRepository, RecipeRepository recipeRepository){
        this.applicationUserRepository = applicationUserRepository;
        this.recipeRepository = recipeRepository;
    }

    /** Create a new recipe for the logged in user (Can also be used to create a new category/tab/type if only the type is specified) */
    @PostMapping
    public void newRecipe(Authentication auth, @RequestBody Recipe recipe){
        ApplicationUser user = applicationUserRepository.findByUsername((auth.getName()));
        recipe.setUser(user);
        recipeRepository.save(recipe);
    }

    /** Get a specific recipe */
    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable long id) throws Exception {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new Exception("This recipe does not exist!"));
        return recipe;
    }

    /** Get all users recipes within each personally user defined types */
    @GetMapping("/types")
    public List<RecipeTypesResponse> getRecipesByType(Authentication auth){
        ApplicationUser user = applicationUserRepository.findByUsername(auth.getName());
        List<Recipe> userRecipeData = recipeRepository.findByUser(user, sort);
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<RecipeTypesResponse> recipeTypesResponseList = new ArrayList<>();
        for(Recipe userRecipe : userRecipeData){
            RecipeResponse recipeResponse = new RecipeResponse(userRecipe);
            if(recipeResponseList.size() > 0 && !recipeResponseList.get(recipeResponseList.size()-1).getType().equals(recipeResponse.getType())){
                RecipeTypesResponse recipeTypesResponse = new RecipeTypesResponse(recipeResponseList.get(recipeResponseList.size()-1).getType(),recipeResponseList);
                recipeTypesResponseList.add(recipeTypesResponse);
                recipeResponseList = new ArrayList<>();
            }
            recipeResponseList.add(recipeResponse);
            if(userRecipe == userRecipeData.get(userRecipeData.size()-1)){
                RecipeTypesResponse recipeTypesResponse = new RecipeTypesResponse(recipeResponseList.get(recipeResponseList.size()-1).getType(),recipeResponseList);
                recipeTypesResponseList.add(recipeTypesResponse);
            }
        }
        return recipeTypesResponseList;
    }

    /** Update a specific recipe */
    @PutMapping("/{id}")
    public void updateRecipe(@PathVariable long id, @RequestBody Recipe requestBody) throws Exception{
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new Exception("This recipe does not exist!"));
        recipeRepository.save(recipe);
    }

    /** Delete a specific recipe */
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable long id) throws Exception {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new Exception("This recipe does not exist!"));
        recipeRepository.delete(recipe);
    }
}
