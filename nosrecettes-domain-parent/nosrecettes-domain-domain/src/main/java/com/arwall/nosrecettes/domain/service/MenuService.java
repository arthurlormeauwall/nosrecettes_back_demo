package com.arwall.nosrecettes.domain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.arwall.nosrecettes.domain.gatesToInfra.api.ManageIngredientFromMenu;
import com.arwall.nosrecettes.domain.gatesToInfra.api.ManageMenuRecipes;
import com.arwall.nosrecettes.domain.gatesToInfra.api.MenuCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.RecipeCrudOperations;
import com.arwall.nosrecettes.domain.model.Menu;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistMenu;

public class MenuService implements ManageMenuRecipes, MenuCrudOperations {


    private static final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);

    PersistMenu menuPersist;

    ManageIngredientFromMenu manageIngredientFromMenu;

    private RecipeCrudOperations recipeCrudOperations;

    public MenuService(PersistMenu menuPersist,
            ManageIngredientFromMenu manageIngredientFromMenu,
            RecipeCrudOperations recipeCrudOperations) {
        this.menuPersist = menuPersist;
        this.manageIngredientFromMenu = manageIngredientFromMenu;
        this.recipeCrudOperations = recipeCrudOperations;
    }

    @Override
    public List<Long> getMenu() {
        return menuPersist.getMenu();
    }

    @Override
    public void addARecipeToMenu(Long id) {
        var recipe = recipeCrudOperations.retrieve(id);
        manageIngredientFromMenu.addIngredientsOfRecipe(recipe);
        menuPersist.addARecipeFromMenu(id);
    }

    @Override
    public void removeRecipeFromMenu(Long id) {
        var recipe = recipeCrudOperations.retrieve(id);
        manageIngredientFromMenu.removeIngredientsOfRecipe(recipe);
        menuPersist.removeRecipeFromMenu(id);
    }

    @Override
    public Boolean isThisRecipeInMenu(Long id) {
        var menus = menuPersist.findAll();
        if (menus.isEmpty()) {
            var newEmptyMenu = Menu.builder().build();
            menuPersist.update(newEmptyMenu);
            LOGGER.warn("Trying to retrieve a recipe from menu but there is no menu ; "
                    + "create a menu");
            return false;
        } else {
            return menus.getFirst().getRecipeIds().stream().anyMatch(ids -> ids.equals(id));
        }
    }

    @Override
    public void hardForceSync(Long recipeId) {
        if (menuPersist.findAll().getFirst().getRecipeIds().stream().anyMatch(id->id.equals(recipeId))){
            manageIngredientFromMenu.hardforceSyncIngredientsFromMenu(recipeCrudOperations.retrieve(recipeId));
        }
    }

    @Override
    public void forceSync(Long recipeId) {
        if (menuPersist.findAll().getFirst().getRecipeIds().stream().anyMatch(id->id.equals(recipeId))){
            manageIngredientFromMenu.forceSyncIngredientsFromMenu(recipeCrudOperations.retrieve(recipeId));
        }
    }
}
