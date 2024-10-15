package com.arwall.nosrecettes.domain.gatesToInfra.api;

import com.arwall.nosrecettes.domain.model.Recipe;

public interface ManageIngredientFromMenu {

    void forceSyncIngredientsFromMenu(Recipe recipe);

    void addIngredientsOfRecipe(Recipe recipe);

    void removeIngredientsOfRecipe(Recipe recipe);

    void hardforceSyncIngredientsFromMenu(Recipe retrieve);
}
