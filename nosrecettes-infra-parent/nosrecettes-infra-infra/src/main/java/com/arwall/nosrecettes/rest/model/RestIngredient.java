package com.arwall.nosrecettes.rest.model;


import com.arwall.nosrecettes.domain.model.Ingredient;

import lombok.Data;

@Data
public class RestIngredient {

    private float quantity;
    private Long itemId;
    private Long id;

    public RestIngredient() {
    }

    public RestIngredient(Ingredient ingredient) {
        this.quantity = ingredient.getQuantity();
        this.itemId = ingredient.getItemId();
        this.id = ingredient.getId();
    }

    public Ingredient toDomain() {
        return Ingredient.builder()
                .withQuantiy(this.quantity)
                .withItemId(this.itemId)
                .withId(this.id)
                .build();
    }
}

