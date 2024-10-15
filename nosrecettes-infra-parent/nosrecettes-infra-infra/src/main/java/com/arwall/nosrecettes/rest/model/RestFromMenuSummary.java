package com.arwall.nosrecettes.rest.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arwall.nosrecettes.domain.model.FromMenuSummary;

import lombok.Data;

@Data
public class RestFromMenuSummary {

    private List<RestIngredientFromMenuSummary> list;
    private Map<String, List<RestIngredientFromMenuSummary>> perrecipe;


    public RestFromMenuSummary(FromMenuSummary fromMenuSummary) {
        this.list = fromMenuSummary.getShoppingList().stream()
                .map(ingredientFromMenuSummary -> new RestIngredientFromMenuSummary(ingredientFromMenuSummary))
                .toList();
        this.perrecipe = new HashMap<>();
        var iterator = fromMenuSummary.getShoppingListPerRecipe().entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            perrecipe.put(
                    entry.getKey(),
                    entry.getValue().stream()
                            .map(ingredient -> new RestIngredientFromMenuSummary(ingredient))
                            .toList());
        }
    }
}
