package com.arwall.nosrecettes.domain.stubfactories;

import java.util.List;

import com.arwall.nosrecettes.domain.model.Ingredient;
import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.RecipeSummary;
import com.arwall.nosrecettes.domain.model.Season;

public class RecipeStubs {
    public static final String A_SOURCE = "a_source";
    public static final Float A_QUANTITY = 4F;
    public static final Long AN_INGREDIENT_ID = 3L;
    public static final Ingredient AN_INGREDIENT = Ingredient.builder()
            .withItemId(ItemStubs.AN_ITEM_ID)
            .withQuantiy(A_QUANTITY)
            .withId(AN_INGREDIENT_ID)
            .build();
    public static final Long A_RECIPE_ID = 5L;
    public static final String A_RECIPE_NAME = "a_recipe_name";
    public static final String A_TYPE = "a_type";
    public static final Season A_SEASON = Season.SUMMER;
    public static final Recipe A_RECIPE= Recipe.builder()
            .withId(A_RECIPE_ID)
            .withSource(A_SOURCE)
            .withIngredients(List.of(AN_INGREDIENT))
            .withName(A_RECIPE_NAME)
            .withType(A_TYPE)
            .withSeason(A_SEASON)
            .build();
    public static final Long A_NEW_RECIPE_ID = 6L;
    private static final Long AN_OTHER_INGREDIENT_ID = 11L;
    private static final Long AN_OTHER_ITEM_ID = 14L;
    private static final Ingredient AN_OTHER_INGREDIENT = Ingredient.builder()
            .withId(AN_OTHER_INGREDIENT_ID)
            .withItemId(AN_OTHER_ITEM_ID)
            .withQuantiy(A_QUANTITY)
            .build();
    public static final Long AN_OTHER_RECIPE_ID = 12L;
    public static final Recipe AN_OTHER_RECIPE= Recipe.builder()
            .withId(AN_OTHER_RECIPE_ID)
            .withSource(A_SOURCE)
            .withIngredients(List.of(AN_OTHER_INGREDIENT))
            .withName(A_RECIPE_NAME)
            .withType(A_TYPE)
            .withSeason(A_SEASON)
            .build();
    public static final RecipeSummary A_RECIPE_SUMMARY = RecipeSummary.builder()
            .withId(A_RECIPE_ID)
            .withSource(A_SOURCE)
            .withName(A_RECIPE_NAME)
            .withType(A_TYPE)
            .withSeason(A_SEASON)
            .build();
    public static final Recipe A_NEW_RECIPE= Recipe.builder()
            .withId(A_NEW_RECIPE_ID)
            .build();
}
