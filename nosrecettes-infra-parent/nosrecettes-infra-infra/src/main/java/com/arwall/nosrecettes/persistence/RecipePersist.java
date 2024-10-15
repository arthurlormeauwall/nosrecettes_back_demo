package com.arwall.nosrecettes.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.RecipeSummary;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistRecipe;
import com.arwall.nosrecettes.persistence.model.IngredientData;
import com.arwall.nosrecettes.persistence.model.RecipeData;
import com.arwall.nosrecettes.persistence.repo.IngredientRepository;
import com.arwall.nosrecettes.persistence.repo.RecipeRepository;

@Component
public class RecipePersist implements PersistRecipe {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Override
    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id).map(RecipeData::toDomain);
    }

    @Override
    public List<RecipeSummary> retrieveALlRecipeSummary() {
        return recipeRepository.findAll().stream().map(RecipeData::toDomainRecipeSummary).toList();
    }

    @Override
    public Recipe update(Recipe recipe) {
        return recipeRepository.save(new RecipeData(recipe)).toDomain();
    }

    @Override
    public Recipe create() {
        return recipeRepository.save(new RecipeData()).toDomain();
    }

    @Override
    public void delete(long id) {
        recipeRepository.deleteById(id);
    }
}
