package com.jnk2016.happyplannerbackend.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeTypesResponse {
    private String type;
    private List<RecipeResponse> recipes;

}
