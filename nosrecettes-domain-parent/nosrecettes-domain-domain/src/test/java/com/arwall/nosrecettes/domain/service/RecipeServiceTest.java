package com.arwall.nosrecettes.domain.service;

import static com.arwall.nosrecettes.domain.stubfactories.ItemStubs.AN_ITEM_ID;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.AN_INGREDIENT;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_NEW_RECIPE;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_NEW_RECIPE_ID;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_RECIPE;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_RECIPE_ID;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_RECIPE_NAME;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_RECIPE_SUMMARY;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_SEASON;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_SOURCE;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistRecipe;
import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.RecipeSummary;

class RecipeServiceTest {

    RecipeService fixture;

    PersistRecipe recipePersist;

    @BeforeEach
    void setup() {
        recipePersist = Mockito.mock(PersistRecipe.class);
        fixture = new RecipeService(recipePersist);
    }

    @Test
    void should_retrieve_recipe() {
        // Given
        Long id = AN_ITEM_ID;
        Mockito.when(recipePersist.findById(id)).thenReturn(Optional.of(A_RECIPE));

        // When
        var actual = fixture.retrieve(id);

        // Then
        assertThat(actual).isEqualTo(A_RECIPE);
    }

    @Test
    void should_delete_item() {
        // Given // When
        fixture.delete(A_RECIPE_ID);

        // Then
        Mockito.verify(recipePersist).delete(A_RECIPE_ID);
    }

    @Test
    void should_update_already_existed_item() {
        // Given // When
        fixture.update(A_RECIPE);

        // Then
        Mockito.verify(recipePersist).update(A_RECIPE);
    }

    @Test
    void should_create_and_save_when_update_new_item() {
        // Given
        var aRecipeWithoutId = Recipe.builder().withSource(A_SOURCE).withIngredients(List.of(AN_INGREDIENT))
                .withName(A_RECIPE_NAME).withType(A_TYPE).withSeason(A_SEASON).build();
        var expected = Recipe.builder().withId(A_NEW_RECIPE_ID).withSource(A_SOURCE)
                .withIngredients(List.of(AN_INGREDIENT)).withName(A_RECIPE_NAME).withType(A_TYPE).withSeason(A_SEASON)
                .build();
        Mockito.when(recipePersist.create()).thenReturn(A_NEW_RECIPE);
        Mockito.when(recipePersist.update(ArgumentMatchers.any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, Recipe.class));

        // When
        var actual = fixture.update(aRecipeWithoutId);

        // Then
        assertThat(actual.isTheSameEntityAs(expected)).isTrue();
    }

    @Test
    void should_find_all_items() {
        // Given
        List<RecipeSummary> allRecipesSummary = List.of(A_RECIPE_SUMMARY);
        Mockito.when(recipePersist.retrieveALlRecipeSummary()).thenReturn(allRecipesSummary);

        // When
        var actual = fixture.findAll();

        // Then
        Mockito.verify(recipePersist).retrieveALlRecipeSummary();
        assertThat(actual).isEqualTo(allRecipesSummary);
    }
}