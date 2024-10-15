package com.arwall.nosrecettes.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arwall.nosrecettes.domain.stubs.InMemoryIngredientFromMenuPersist;
import com.arwall.nosrecettes.domain.stubs.InMemoryMenuPersist;
import com.arwall.nosrecettes.domain.stubs.InMemoryRecipePersist;

class MenuServiceTest {

    MenuService fixture;

    @BeforeEach
    void setup() {
        fixture = new MenuService(
                new InMemoryMenuPersist(),
                new IngredientFromMenuService(new InMemoryIngredientFromMenuPersist()),
                new RecipeService(new InMemoryRecipePersist())
        );
    }

    @Test
    void should_get_recipe_summaries() {
        // Given

        // When

        // Then
    }

    @Test
    void addARecipe() {
        // Given

        // When

        // Then
    }

    @Test
    void remove() {
        // Given

        // When

        // Then
    }

    @Test
    void isThisRecipeInMenu() {
        // Given

        // When

        // Then
    }

    @Test
    void forceSync() {
        // Given

        // When

        // Then
    }
}