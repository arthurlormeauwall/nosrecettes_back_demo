package com.arwall.nosrecettes.rest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.Season;

import lombok.Data;

@Data
public class RestRecipe {

    private Long id;
    private String name;
    private String source;
    private Season season;
    private String type;
    private List<RestIngredient> ingredients;

    public RestRecipe(Recipe recipe) {
        this.name = recipe.getName();
        this.ingredients = Optional.ofNullable(recipe.getIngredients())
                .map(ingredients -> ingredients.stream()
                        .map(ingredient -> new RestIngredient(ingredient)).toList())
                .orElse(null);
        this.season = recipe.getSeason();
        this.type = recipe.getType();
        this.source = recipe.getSource();
        this.id = recipe.getId();
    }

    public RestRecipe() {
        this.ingredients = new ArrayList<>();
    }

    public Recipe toDomain() {
        return Recipe.builder()
                .withName(this.name)
                .withSeason(this.season)
                .withIngredients(Optional.ofNullable(this.ingredients).map(ing -> ing.stream().map(ingredient ->
                        ingredient.toDomain()).toList()).orElse(null))
                .withSource(this.source)
                .withType(this.type)
                .withId(this.id)
                .build();
    }
}
