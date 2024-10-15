package com.arwall.nosrecettes.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.arwall.nosrecettes.domain.gatesToInfra.api.GetIngredientFromMenuSummary;
import com.arwall.nosrecettes.domain.gatesToInfra.api.IngredientFromMenuCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.ItemCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.MenuCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.RecipeCrudOperations;
import com.arwall.nosrecettes.domain.model.FromMenuSummary;
import com.arwall.nosrecettes.domain.model.IngredientFromMenuSummary;
import com.arwall.nosrecettes.domain.model.Recipe;


public class ShoppingListSummaryService implements GetIngredientFromMenuSummary {

    
    private final MenuCrudOperations menuCrudOperations;
    private final RecipeCrudOperations recipeCrudOperations;
    private final ItemCrudOperations itemService;
    private final IngredientFromMenuCrudOperations ingredientFromMenuService;

    public ShoppingListSummaryService(
            MenuCrudOperations menuCrudOperations,
            RecipeCrudOperations recipeCrudOperations,
            ItemCrudOperations itemCrudOperations,
            IngredientFromMenuCrudOperations ingredientFromMenuCrudOperations) {
        this.menuCrudOperations = menuCrudOperations;
        this.recipeCrudOperations = recipeCrudOperations;
        this.itemService = itemCrudOperations;
        this.ingredientFromMenuService = ingredientFromMenuCrudOperations;
    }

    @Override
    public FromMenuSummary getIngredientFromMenuSummary() {
        var shoppingList = new ArrayList<IngredientFromMenuSummary>();
        var shoppingListPerRecipe = new HashMap<String, List<IngredientFromMenuSummary>>();
        var recipesFromMenu = menuCrudOperations.getMenu().stream()
                .map(id -> recipeCrudOperations.retrieve(id))
                .map(Recipe::getSummary).toList();
        recipesFromMenu.stream().forEach(recipeSummary ->
                shoppingListPerRecipe.put(recipeSummary.getName(),
                        recipeCrudOperations.retrieve(recipeSummary.getId()).getIngredients()
                                .stream()
                                .map(ingredient -> {
                                    var it = itemService.retrieve(ingredient.getItemId());
                                    return IngredientFromMenuSummary.builder()
                                            .withQuantiy(ingredient.getQuantity())
                                            .withName(it.getName())
                                            .build();
                                })
                                .toList())
        );

        ingredientFromMenuService.findAll().stream()
                .forEach(ing -> {
                    var it = itemService.retrieve(ing.getItemId());
                    shoppingList.add(IngredientFromMenuSummary.builder().withQuantiy(ing.getQuantity())
                            .withName(it.getName())
                            .build());
                });

        return FromMenuSummary.builder()
                .withShoppingList(shoppingList)
                .withShoppingListPerRecipe(shoppingListPerRecipe)
                .build();
    }
}
