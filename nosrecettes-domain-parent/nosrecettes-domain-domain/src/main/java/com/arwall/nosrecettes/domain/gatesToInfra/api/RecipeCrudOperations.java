package com.arwall.nosrecettes.domain.gatesToInfra.api;

import java.util.List;

import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.RecipeSummary;

public interface RecipeCrudOperations {

    Recipe retrieve(Long id);

    List<RecipeSummary> findAll();

    Recipe update(Recipe recipe);

    void delete(long id);
}
