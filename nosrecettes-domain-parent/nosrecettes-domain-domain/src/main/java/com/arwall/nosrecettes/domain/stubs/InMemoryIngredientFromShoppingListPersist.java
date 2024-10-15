package com.arwall.nosrecettes.domain.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.IngredientFromShoppingList;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistIngredientFromShoppingList;

public class InMemoryIngredientFromShoppingListPersist implements PersistIngredientFromShoppingList {

    Map<Long, IngredientFromShoppingList> repository;

    public InMemoryIngredientFromShoppingListPersist() {
        repository = new HashMap<>();
    }

    @Override
    public Optional<IngredientFromShoppingList> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<IngredientFromShoppingList> findAll() {
        var response = new ArrayList();
        var it = repository.entrySet().iterator();
        while (it.hasNext()) {
            response.add(it.next().getValue());
        }
        return response;
    }

    @Override
    public IngredientFromShoppingList update(IngredientFromShoppingList ingredient) {
        repository.put(ingredient.getId(), ingredient);
        return ingredient;
    }

    @Override
    public void delete(Long id) {
        repository.remove(id);
    }
}
