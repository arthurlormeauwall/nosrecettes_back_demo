package com.arwall.nosrecettes.domain.gatesToInfra.api;

public interface ManageMenuRecipes {

    void addARecipeToMenu(Long id);

    void removeRecipeFromMenu(Long id);

    void forceSync(Long recipeId);

    Boolean isThisRecipeInMenu(Long id);

    void hardForceSync(Long id);
}
