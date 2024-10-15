package com.arwall.nosrecettes.rest.model;

import com.arwall.nosrecettes.domain.model.IngredientFromMenuSummary;

import lombok.Data;

@Data
public class RestIngredientFromMenuSummary {

    private float quantity;
    private String name;


    public RestIngredientFromMenuSummary() {
    }

    public RestIngredientFromMenuSummary(IngredientFromMenuSummary ingredient) {
        this.quantity = ingredient.getQuantity();
        this.name = ingredient.getName();
    }
}
