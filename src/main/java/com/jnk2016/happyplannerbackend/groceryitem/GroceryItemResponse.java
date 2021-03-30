package com.jnk2016.happyplannerbackend.groceryitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryItemResponse {
    private long groceryId;
    private String name;
    private int type;

    public GroceryItemResponse(GroceryItem groceryItem){
        this.groceryId = groceryItem.getGroceryId();
        this.name = groceryItem.getName();
        this.type = groceryItem.getType();
    }
}
