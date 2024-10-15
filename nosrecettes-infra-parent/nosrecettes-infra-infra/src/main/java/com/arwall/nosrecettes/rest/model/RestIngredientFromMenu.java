package com.arwall.nosrecettes.rest.model;


import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arwall.nosrecettes.domain.model.IngredientFromMenu;

import lombok.Data;

@Data
public class RestIngredientFromMenu {

    private Map<Long, Float> quantities;
    private Long itemId;
    private Long id;
    private Set<Long> recipeId;

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    private Float quantity;

    public RestIngredientFromMenu() {
    }

    public RestIngredientFromMenu(IngredientFromMenu ingredient) {
        this.quantities = ingredient.getQuantitiesPerRecipes();
        this.itemId = ingredient.getItemId();
        this.id = ingredient.getId();
        this.recipeId = ingredient.getRecipeId();
        this.quantity = ingredient.getQuantity();
    }

    public IngredientFromMenu toDomain() {
        var domainIngredient = IngredientFromMenu.builder()
                .withQuanties(this.quantities)
                .withItemId(this.itemId)
                .withId(this.id)
                .withRecipeId(this.recipeId)
                .build();
        var rectification = this.quantity - domainIngredient.getQuantity();
        return IngredientFromMenu.builder(domainIngredient)
                .withRectification(rectification)
                .build();
    }
}

