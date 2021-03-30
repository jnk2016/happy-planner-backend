package com.jnk2016.happyplannerbackend.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {
    private long recipeId;
    private String title;
    private String type;
    private long preptime;
    private long cooktime;

    private String preparation;
    private String ingredients;
    private String notes;

    public RecipeResponse(Recipe recipe){
        this.recipeId = recipe.getRecipeId();
        this.title = recipe.getTitle();
        this.type = recipe.getType();
        this.preptime = recipe.getPreptime();
        this.cooktime = recipe.getCooktime();

        this.preparation = recipe.getPreparation();
        this.ingredients = recipe.getIngredients();
        this.notes = recipe.getNotes();
    }
}
