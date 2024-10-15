package com.arwall.nosrecettes.domain.service;

import java.util.List;

import com.arwall.nosrecettes.domain.gatesToInfra.api.IngredientFromShoppingListCrudOperations;
import com.arwall.nosrecettes.domain.exception.IngredientNotFoundException;
import com.arwall.nosrecettes.domain.model.IngredientFromShoppingList;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistIngredientFromShoppingList;

public class ShoppingListService implements IngredientFromShoppingListCrudOperations {

    private final PersistIngredientFromShoppingList ingredientPersist;


    public ShoppingListService(PersistIngredientFromShoppingList ingredientPersist) {
        this.ingredientPersist = ingredientPersist;
    }



    @Override
    public List<IngredientFromShoppingList> findAll() {
        return ingredientPersist.findAll();
    }

    @Override
    public IngredientFromShoppingList findById(Long id) {
        var ingOpt = ingredientPersist.findById(id);
        if (ingOpt.isPresent()) {
            return ingOpt.get();
        } else {
            throw new IngredientNotFoundException(id);
        }
    }

    @Override
    public IngredientFromShoppingList update(IngredientFromShoppingList ingredient) {
        Long id = ingredient.getId();

        if (null == id) {
            var emptyIngredient = IngredientFromShoppingList.builder().build();
            var savedIngredientrepository = ingredientPersist.update(emptyIngredient);
            id = savedIngredientrepository.getId();
            ingredient = IngredientFromShoppingList.builder(ingredient).withId(id).build();
        }

        return ingredientPersist.update(ingredient);
    }

    @Override
    public void delete(Long id) {
        ingredientPersist.delete(id);
    }
}
