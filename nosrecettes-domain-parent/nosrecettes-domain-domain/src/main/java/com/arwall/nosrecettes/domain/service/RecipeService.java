package com.arwall.nosrecettes.domain.service;

import java.util.List;

import com.arwall.nosrecettes.domain.gatesToInfra.api.RecipeCrudOperations;
import com.arwall.nosrecettes.domain.exception.RecipeNotFoundException;
import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.RecipeSummary;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistRecipe;

public class RecipeService implements RecipeCrudOperations {

    PersistRecipe recipePersist;

    public RecipeService(PersistRecipe recipePersist) {
        this.recipePersist = recipePersist;
    }

    @Override
    public Recipe retrieve(Long id) {
        var recipeOpt = recipePersist.findById(id);
        if (recipeOpt.isPresent()) {
            return recipeOpt.get();
        } else {
            throw new RecipeNotFoundException(id);
        }
    }

    @Override
    public List<RecipeSummary> findAll() {
        return recipePersist.retrieveALlRecipeSummary();
    }

    @Override
    public Recipe update(Recipe recipe) {
        Long id = recipe.getId();
        if (null == id) {
            var savedEmptyRecipe = recipePersist.create();
            id = savedEmptyRecipe.getId();
            recipe = Recipe.builder(recipe).withId(id).build();
        }
        return recipePersist.update(recipe);
    }

    @Override
    public void delete(long id) {
        recipePersist.delete(id);
    }
}
