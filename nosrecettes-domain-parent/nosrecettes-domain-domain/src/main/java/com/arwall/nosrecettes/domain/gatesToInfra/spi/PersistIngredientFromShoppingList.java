package com.arwall.nosrecettes.domain.gatesToInfra.spi;

import java.util.List;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.IngredientFromShoppingList;

public interface PersistIngredientFromShoppingList {

    Optional<IngredientFromShoppingList> findById(Long id);

    List<IngredientFromShoppingList> findAll();

    IngredientFromShoppingList update(IngredientFromShoppingList Ingredient);

    void delete(Long id);
}
