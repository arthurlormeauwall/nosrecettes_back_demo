package com.arwall.nosrecettes.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.arwall.nosrecettes.domain.gatesToInfra.api.IngredientFromMenuCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.ManageIngredientFromMenu;
import com.arwall.nosrecettes.domain.exception.IngredientNotFoundException;
import com.arwall.nosrecettes.domain.model.Ingredient;
import com.arwall.nosrecettes.domain.model.IngredientFromMenu;
import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistIngredientFromMenu;

public class IngredientFromMenuService implements
        IngredientFromMenuCrudOperations,
        ManageIngredientFromMenu {

    PersistIngredientFromMenu ingredientFromMenuPersist;

    public IngredientFromMenuService(PersistIngredientFromMenu ingredientFromMenuPersist) {
        this.ingredientFromMenuPersist = ingredientFromMenuPersist;
    }

    @Override
    public void forceSyncIngredientsFromMenu(Recipe recipe) {
        // REMOVE
        ingredientFromMenuPersist.retrieveAllIngredientFromMenu()
                .forEach(ingredient -> {
                    var ingredientFromRecipe = recipe.getIngredients().stream()
                            .filter(recipeIngredient -> recipeIngredient.getItemId().equals(ingredient.getItemId()))
                            .findFirst();
                    if (ingredientFromRecipe.isPresent()) {
                        ingredient.getQuantitiesPerRecipes().remove(recipe.getId());
                        ingredient.getRecipeId().remove(recipe.getId());
                        var newIngredient = IngredientFromMenu.builder(ingredient)
                                .build();
                        ingredientFromMenuPersist.update(newIngredient);
                    }
                });

        // ADD
        var recipeId = recipe.getId();
        recipe.getIngredients().stream().forEach(ingredient -> {
            var ingredientsFromMenu = ingredientFromMenuPersist.retrieveAllIngredientFromMenu();
            var alreadyExistedIngredient = ingredientsFromMenu.stream().filter(ingredientFromMenu ->
                            ingredientFromMenu.getItemId().equals(ingredient.getItemId()))
                    .findAny();
            IngredientFromMenu updatedIngredient = null;
            if (alreadyExistedIngredient.isPresent()) {
                var ingredientFromMenu = alreadyExistedIngredient.get();
                ingredientFromMenu.getRecipeId().add(recipeId);
                ingredientFromMenu.getQuantitiesPerRecipes().put(recipeId, ingredient.getQuantity());
                updatedIngredient = IngredientFromMenu.builder(ingredientFromMenu)
                        .build();
            } else {
                Set<Long> recipeIds = new HashSet<>();
                recipeIds.add(recipeId);
                var quantities = new HashMap();
                quantities.put(recipeId, ingredient.getQuantity());
                updatedIngredient = IngredientFromMenu.builder()
                        .withQuanties(quantities)
                        .withItemId(ingredient.getItemId())
                        .withRecipeId(recipeIds)
                        .build();
            }
            if (updatedIngredient.getQuantity() <= 0) {
                ingredientFromMenuPersist.delete(updatedIngredient.getId());
            } else {
                ingredientFromMenuPersist.update(updatedIngredient);
            }
        });
    }

    @Override
    public void hardforceSyncIngredientsFromMenu(Recipe recipe) {
        udpateAllRectificationsToZero(recipe);
        forceSyncIngredientsFromMenu(recipe);
    }

    private void udpateAllRectificationsToZero(Recipe recipe) {
        recipe.getIngredients().stream().forEach(ingredient -> {
            var ingredientFromMenuWithGoodId = ingredientFromMenuPersist.retrieveAllIngredientFromMenu().stream()
                    .filter(ingredientFromMenu ->
                            ingredientFromMenu.getItemId().equals(ingredient.getItemId()))
                    .findAny();
            if (ingredientFromMenuWithGoodId.isPresent()) {
                var ingredientFromMenu = ingredientFromMenuWithGoodId.get();
                var updatedIngredient = IngredientFromMenu.builder(ingredientFromMenu)
                        .withRectification(0F)
                        .build();
                ingredientFromMenuPersist.update(updatedIngredient);
            }
        });
    }

    @Override
    public void addIngredientsOfRecipe(Recipe recipe) {
        recipe.getIngredients().stream().forEach(addAnIngredient(recipe.getId()));
    }


    private Consumer<Ingredient> addAnIngredient(Long recipeId) {
        return ingredient -> {
            var ingredientsFromMenu = ingredientFromMenuPersist.retrieveAllIngredientFromMenu();
            var alreadyExistedIngredient = ingredientsFromMenu.stream().filter(ingredientFromMenu ->
                            ingredientFromMenu.getItemId().equals(ingredient.getItemId()))
                    .findAny();
            if (alreadyExistedIngredient.isPresent()) {
                var ingredientFromMenu = alreadyExistedIngredient.get();
                ingredientFromMenu.getRecipeId().add(recipeId);
                ingredientFromMenu.getQuantitiesPerRecipes().put(recipeId, ingredient.getQuantity());
                var updatedIngredient = IngredientFromMenu.builder(ingredientFromMenu)
                        .build();
                ingredientFromMenuPersist.update(updatedIngredient);
            } else {
                Set<Long> recipeIds = new HashSet<>();
                recipeIds.add(recipeId);
                var quantities = new HashMap();
                quantities.put(recipeId, ingredient.getQuantity());
                ingredientFromMenuPersist.update(IngredientFromMenu.builder()
                        .withQuanties(quantities)
                        .withItemId(ingredient.getItemId())
                        .withRecipeId(recipeIds)
                        .build());
            }
        };
    }

    @Override
    public void removeIngredientsOfRecipe(Recipe recipe) {
        ingredientFromMenuPersist.retrieveAllIngredientFromMenu()
                .forEach(removeAnIngredientLinkedToArecipe(recipe));
    }


    private Consumer<IngredientFromMenu> removeAnIngredientLinkedToArecipe(Recipe recipe) {
        return ingredient -> {
            var ingredientFromRecipe = recipe.getIngredients().stream()
                    .filter(recipeIngredient -> recipeIngredient.getItemId().equals(ingredient.getItemId()))
                    .findFirst();
            if (ingredientFromRecipe.isPresent()) {
                ingredient.getQuantitiesPerRecipes().remove(recipe.getId());
                ingredient.getRecipeId().remove(recipe.getId());
                if (ingredient.getQuantity() <= 0) {
                    ingredientFromMenuPersist.delete(ingredient.getId());
                } else {
                    ingredientFromMenuPersist.update(IngredientFromMenu.builder(ingredient)
                            .build());
                }
            }
        };
    }

    @Override
    public List<IngredientFromMenu> findAll() {
        return ingredientFromMenuPersist.retrieveAllIngredientFromMenu();
    }

    @Override
    public IngredientFromMenu retrieve(Long id) {
        var ingOpt = ingredientFromMenuPersist.findById(id);
        if (ingOpt.isPresent()) {
            return ingOpt.get();
        } else {
            throw new IngredientNotFoundException(id);
        }
    }

    @Override
    public IngredientFromMenu update(IngredientFromMenu ingredient) {
        Long id = ingredient.getId();
        if (null == id) {
            var savedEmptyRecipe = ingredientFromMenuPersist.create();
            id = savedEmptyRecipe.getId();
            ingredient = IngredientFromMenu.builder(ingredient).withId(id).build();
        }
        return ingredientFromMenuPersist.update(ingredient);
    }

    @Override
    public void delete(Long id) {
        ingredientFromMenuPersist.delete(id);
    }
}
