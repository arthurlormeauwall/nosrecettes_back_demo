package com.arwall.nosrecettes.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import com.arwall.nosrecettes.domain.ddd.DomainEntity;

@Getter
public class Recipe extends DomainEntity {

    private final String name;
    private final String source;
    private final Season season;
    private final String type;
    private final List<Ingredient> ingredients;

    private Recipe(Builder builder) {
        super(builder.id);
        this.ingredients = builder.ingredients;
        this.name = builder.name;
        this.season = builder.season;
        this.type = builder.type;
        this.source = builder.source;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Recipe recipe) {
        return new Builder(recipe);
    }

    public RecipeSummary getSummary() {
        return RecipeSummary.builder(this).build();
    }

    public static class Builder {

        private Long id;
        private String name;
        private String source;
        private Season season;
        private String type;
        private List<Ingredient> ingredients;

        public Builder() {
            this.ingredients=new ArrayList<>();
        }

        public Builder(Recipe recipe) {
            this.id = recipe.getId();
            this.type = recipe.getType();
            this.name = recipe.getName();
            this.season = recipe.getSeason();
            this.source = recipe.getSource();
            this.ingredients = recipe.getIngredients();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withSource(String source) {
            this.source = source;
            return this;
        }

        public Builder withSeason(Season season) {
            this.season = season;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }
    }
}
