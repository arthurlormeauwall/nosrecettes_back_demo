package com.arwall.nosrecettes.domain.gatesToInfra.spi;

import java.util.List;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.RecipeSummary;

public interface PersistRecipe {

    Optional<Recipe> findById(Long id);

    List<RecipeSummary> retrieveALlRecipeSummary();

    Recipe update(Recipe recipe);

    void delete(long id);

    Recipe create();
}
