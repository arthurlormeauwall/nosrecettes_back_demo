package com.arwall.nosrecettes.domain.gatesToInfra.api;

import java.util.List;

import com.arwall.nosrecettes.domain.model.IngredientFromShoppingList;

public interface IngredientFromShoppingListCrudOperations {
    List<IngredientFromShoppingList> findAll();

    IngredientFromShoppingList findById(Long id);

    IngredientFromShoppingList update(IngredientFromShoppingList ingredient);

    void delete(Long id);

}
