package com.arwall.nosrecettes.domain.service;

import static com.arwall.nosrecettes.domain.stubfactories.IngredientFromMenuStubs.AN_INGREDIENT_FROM_MENU;
import static com.arwall.nosrecettes.domain.stubfactories.IngredientFromMenuStubs.A_NEW_INGREDIENT_FROM_MENU;
import static com.arwall.nosrecettes.domain.stubfactories.IngredientFromMenuStubs.A_NEW_INGREDIENT_FROM_MENU_ID;
import static com.arwall.nosrecettes.domain.stubfactories.ItemStubs.AN_ITEM_ID;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.AN_INGREDIENT;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.AN_OTHER_RECIPE;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.AN_OTHER_RECIPE_ID;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_QUANTITY;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_RECIPE;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_RECIPE_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.arwall.nosrecettes.domain.model.Ingredient;
import com.arwall.nosrecettes.domain.model.IngredientFromMenu;
import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistIngredientFromMenu;
import com.arwall.nosrecettes.domain.stubs.InMemoryIngredientFromMenuPersist;

class IngredientFromMenuServiceTest {

    IngredientFromMenuService fixture;

    PersistIngredientFromMenu ingredientFromMenuPersist;

    @BeforeEach
    void setup() {
        ingredientFromMenuPersist = Mockito.mock(PersistIngredientFromMenu.class);
        fixture = new IngredientFromMenuService(ingredientFromMenuPersist);
    }

    @Test
    void should_retrieve_ingredient_from_menu() {
        // Given
        Long id = AN_ITEM_ID;
        Mockito.when(ingredientFromMenuPersist.findById(id))
                .thenReturn(Optional.of(AN_INGREDIENT_FROM_MENU));

        // When
        var actual = fixture.retrieve(id);

        // Then
        assertThat(actual).isEqualTo(AN_INGREDIENT_FROM_MENU);
    }


    @Test
    void should_update_already_existed_ingredient_from_menu() {
        // Given // When
        var actual = fixture.update(AN_INGREDIENT_FROM_MENU);

        // Then
        Mockito.verify(ingredientFromMenuPersist).update(AN_INGREDIENT_FROM_MENU);
    }

    @Test
    void should_create_and_save_when_update_new_ingredient_from_menu() {
        // Given
        var anIngredientWithoutIdQuantities = new HashMap<Long, Float>();
        anIngredientWithoutIdQuantities.put(A_RECIPE_ID, A_QUANTITY);
        var anIngredientFromMenuWithoutId = IngredientFromMenu.builder()
                .withRecipeId(Set.of(A_RECIPE_ID))
                .withQuanties(anIngredientWithoutIdQuantities)
                .withItemId(AN_ITEM_ID)
                .build();
        var expected = IngredientFromMenu.builder()
                .withRecipeId(Set.of(A_RECIPE_ID))
                .withQuanties(anIngredientWithoutIdQuantities)
                .withItemId(AN_ITEM_ID)
                .withId(A_NEW_INGREDIENT_FROM_MENU_ID)
                .build();
        Mockito.when(ingredientFromMenuPersist.create()).thenReturn(A_NEW_INGREDIENT_FROM_MENU);
        Mockito.when(ingredientFromMenuPersist.update(ArgumentMatchers.any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, IngredientFromMenu.class));

        // When
        var actual = fixture.update(anIngredientFromMenuWithoutId);

        // Then
        assertThat(actual.isTheSameEntityAs(expected)).isTrue();
    }

    @Test
    void should_find_all_ingredient_from_menu() {
        // Given
        List<IngredientFromMenu> allIngredientFromMenu = List.of(AN_INGREDIENT_FROM_MENU);
        Mockito.when(ingredientFromMenuPersist.retrieveAllIngredientFromMenu()).thenReturn(allIngredientFromMenu);

        // When
        var actual = fixture.findAll();

        // Then
        Mockito.verify(ingredientFromMenuPersist).retrieveAllIngredientFromMenu();
        assertThat(actual).isEqualTo(allIngredientFromMenu);
    }

    @Test
    void should_add_Ingredients_Of_Recipe() {
        // Given
        fixture = new IngredientFromMenuService(new InMemoryIngredientFromMenuPersist());

        // When
        fixture.addIngredientsOfRecipe(A_RECIPE);
        fixture.addIngredientsOfRecipe(AN_OTHER_RECIPE);
        var ingredientFromMenu = fixture.findAll();

        // Then
        A_RECIPE.getIngredients().forEach(ingredient -> {
            assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                    ingredientfm.getItemId().equals(ingredient.getItemId()) &&
                            ingredientfm.getQuantity() == (ingredient.getQuantity())))
                    .isTrue();
        });
        AN_OTHER_RECIPE.getIngredients().forEach(ingredient -> {
            assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                    ingredientfm.getItemId().equals(ingredient.getItemId()) &&
                            ingredientfm.getQuantity() == (ingredient.getQuantity())))
                    .isTrue();
        });
    }

    @Test
    void should_add_Ingredients_Of_2_Recipes_and_overlap_ingredient() {
        // Given
        fixture = new IngredientFromMenuService(new InMemoryIngredientFromMenuPersist());
        long anotherIngredientItemId = 23L;
        float anotherIngredientQuantity = 4F;
        Ingredient newIngredient = Ingredient.builder()
                .withItemId(anotherIngredientItemId)
                .withQuantiy(anotherIngredientQuantity).build();
        var otherRecipe = Recipe.builder(A_RECIPE)
                .withIngredients(List.of(AN_INGREDIENT,
                        newIngredient))
                .withId(AN_OTHER_RECIPE_ID)
                .build();

        // When
        fixture.addIngredientsOfRecipe(A_RECIPE);
        fixture.addIngredientsOfRecipe(otherRecipe);
        var ingredientFromMenu = fixture.findAll();

        // Then
        assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                ingredientfm.getItemId().equals(anotherIngredientItemId) &&
                        ingredientfm.getQuantity() == (anotherIngredientQuantity)))
                .isTrue();
        float newQuantityExpected = A_QUANTITY + A_QUANTITY;
        assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                ingredientfm.getItemId().equals(AN_ITEM_ID) &&
                        ingredientfm.getQuantity() == newQuantityExpected &&
                        ingredientfm.getRecipeId().containsAll(List.of(A_RECIPE_ID, AN_OTHER_RECIPE_ID))))
                .isTrue();

    }

    @Test
    void should_remove_Ingredients_Of_Recipe() {
        fixture = new IngredientFromMenuService(new InMemoryIngredientFromMenuPersist());
        float anotherIngredientQuantity = 4F;
        long anotherIngredientItemId = 23L;
        Ingredient newIngredient = Ingredient.builder()
                .withItemId(anotherIngredientItemId)
                .withQuantiy(anotherIngredientQuantity).build();
        var otherRecipe = Recipe.builder(A_RECIPE)
                .withIngredients(List.of(AN_INGREDIENT,
                        newIngredient))
                .withId(AN_OTHER_RECIPE_ID)
                .build();

        // When
        fixture.addIngredientsOfRecipe(A_RECIPE);
        fixture.addIngredientsOfRecipe(otherRecipe);
        fixture.removeIngredientsOfRecipe(A_RECIPE);
        var ingredientFromMenu = fixture.findAll();

        // Then
        assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                ingredientfm.getItemId().equals(anotherIngredientItemId) &&
                        ingredientfm.getQuantity() == anotherIngredientQuantity))
                .isTrue();
        assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                ingredientfm.getItemId().equals(AN_ITEM_ID) &&
                        ingredientfm.getQuantity().equals(A_QUANTITY) &&
                        ingredientfm.getRecipeId().containsAll(List.of(AN_OTHER_RECIPE_ID))))
                .isTrue();
    }

    @Test
    void should_remove_Ingredients_Of_Recipe_after_updated_ingredient_of_recipe() {
        fixture = new IngredientFromMenuService(new InMemoryIngredientFromMenuPersist());

        float anotherIngredientQuantity = 4F;
        long anotherIngredientItemId = 23L;
        var newIngredient = Ingredient.builder()
                .withItemId(anotherIngredientItemId)
                .withQuantiy(anotherIngredientQuantity).build();
        var otherRecipe = Recipe.builder(A_RECIPE)
                .withIngredients(List.of(AN_INGREDIENT,
                        newIngredient))
                .withId(AN_OTHER_RECIPE_ID)
                .build();
        Float newQuantity = A_QUANTITY - 2;
        var updatedRecipe = Recipe.builder(A_RECIPE)
                .withIngredients(List.of(Ingredient.builder(AN_INGREDIENT)
                        .withQuantiy(newQuantity)
                        .build()))
                .build();

        // When
        fixture.addIngredientsOfRecipe(A_RECIPE);
        fixture.addIngredientsOfRecipe(otherRecipe);
        fixture.removeIngredientsOfRecipe(updatedRecipe);
        var ingredientFromMenu = fixture.findAll();

        // Then
        assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                ingredientfm.getItemId().equals(anotherIngredientItemId) &&
                        ingredientfm.getQuantity() == (anotherIngredientQuantity)))
                .isTrue();
        float newQuantityExpected = AN_INGREDIENT.getQuantity();
        assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                ingredientfm.getItemId().equals(AN_ITEM_ID) &&
                        ingredientfm.getQuantity() == newQuantityExpected &&
                        ingredientfm.getRecipeId().containsAll(List.of(AN_OTHER_RECIPE_ID))))
                .isTrue();
    }

    @Test
    void should_update_Ingredients_of_menu() {
        fixture = new IngredientFromMenuService(new InMemoryIngredientFromMenuPersist());

        float anotherIngredientQuantity = 4F;
        long anotherIngredientItemId = 23L;
        var newIngredient = Ingredient.builder()
                .withItemId(anotherIngredientItemId)
                .withQuantiy(anotherIngredientQuantity).build();
        var otherRecipe = Recipe.builder(A_RECIPE)
                .withIngredients(List.of(AN_INGREDIENT,
                        newIngredient))
                .withId(AN_OTHER_RECIPE_ID)
                .build();

        // When
        fixture.addIngredientsOfRecipe(A_RECIPE);
        fixture.addIngredientsOfRecipe(otherRecipe);
        var ingredientFromMenu = fixture.findAll();
        Float rectification = 5F;
        var ingredientToChange = IngredientFromMenu.builder(ingredientFromMenu.get(0))
                .withRectification(rectification)
                .build();
        fixture.removeIngredientsOfRecipe(A_RECIPE);
        fixture.update(ingredientToChange);

        // Then
        ingredientFromMenu = fixture.findAll();

        assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                ingredientfm.getItemId().equals(anotherIngredientItemId) &&
                        ingredientfm.getQuantity() == anotherIngredientQuantity))
                .isTrue();
        var newQuantity = A_QUANTITY + rectification;
        assertThat(ingredientFromMenu.stream().anyMatch(ingredientfm ->
                ingredientfm.getItemId().equals(AN_ITEM_ID) &&
                        (ingredientfm.getQuantity()) == newQuantity &&
                        ingredientfm.getRecipeId().containsAll(List.of(AN_OTHER_RECIPE_ID))))
                .isTrue();
    }
}