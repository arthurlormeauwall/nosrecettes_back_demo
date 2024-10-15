package com.arwall.nosrecettes.rest.model;


import com.arwall.nosrecettes.domain.model.IngredientFromShoppingList;

import lombok.Data;

@Data
public class RestIngredientFromShoppingList {

    private float quantity;
    private Long itemId;
    private Long id;

    public RestIngredientFromShoppingList() {
    }

    public RestIngredientFromShoppingList(IngredientFromShoppingList ingredient) {
        this.quantity = ingredient.getQuantity();
        this.itemId = ingredient.getItemId();
        this.id = ingredient.getId();
    }

    public IngredientFromShoppingList toDomain() {
        return IngredientFromShoppingList.builder()
                .withQuantiy(this.quantity)
                .withItemId(this.itemId)
                .withId(this.id)
                .build();
    }
}

