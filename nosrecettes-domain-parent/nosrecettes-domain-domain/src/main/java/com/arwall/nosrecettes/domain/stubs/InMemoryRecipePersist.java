package com.arwall.nosrecettes.domain.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.RecipeSummary;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistRecipe;

public class InMemoryRecipePersist implements PersistRecipe {

    Map<Long, Recipe> repository;

    public InMemoryRecipePersist() {
        repository = new HashMap<>();
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<RecipeSummary> retrieveALlRecipeSummary() {
        var response = new ArrayList<RecipeSummary>();
        var it = repository.entrySet().iterator();
        while (it.hasNext()) {
            response.add(RecipeSummary.builder(it.next().getValue()).build());
        }
        return response;
    }

    @Override
    public Recipe update(Recipe recipe) {
        repository.put(recipe.getId(), recipe);
        return recipe;
    }

    @Override
    public Recipe create() {
        var it = repository.entrySet().iterator();
        Long nextIndex = null;
        while (it.hasNext()) {
            nextIndex = it.next().getValue().getId() + 1;
        }
        return repository.put(nextIndex, Recipe.builder().build());
    }

    @Override
    public void delete(long id) {
        repository.remove(id);
    }
}
