package com.arwall.nosrecettes.domain.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.IngredientFromMenu;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistIngredientFromMenu;

public class InMemoryIngredientFromMenuPersist implements PersistIngredientFromMenu {

    Map<Long, IngredientFromMenu> repository;

    public InMemoryIngredientFromMenuPersist() {
        repository = new HashMap<>();
    }

    @Override
    public Optional<IngredientFromMenu> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<IngredientFromMenu> retrieveAllIngredientFromMenu() {
        var response = new ArrayList();
        var it = repository.entrySet().iterator();
        while (it.hasNext()) {
            response.add(it.next().getValue());
        }
        return response;
    }

    @Override
    public IngredientFromMenu update(IngredientFromMenu ingredient) {
        if (null == ingredient.getId()) {
            var newingredient = create();
            ingredient = IngredientFromMenu.builder(ingredient).withId(newingredient.getId()).build();
        }
        repository.put(ingredient.getId(), ingredient);
        return ingredient;
    }

    @Override
    public IngredientFromMenu create() {
        var it = repository.entrySet().iterator();
        Long nextIndex = null;
        if (it.hasNext() == false) {
            nextIndex = 0L;
        } else {
            while (it.hasNext()) {
                nextIndex = it.next().getValue().getId() + 1;
            }
        }
        IngredientFromMenu newIngredient = IngredientFromMenu.builder().withId(nextIndex).build();
        repository.put(nextIndex, newIngredient);
        return newIngredient;
    }

    @Override
    public void delete(Long id) {
        repository.remove(id);
    }
}
