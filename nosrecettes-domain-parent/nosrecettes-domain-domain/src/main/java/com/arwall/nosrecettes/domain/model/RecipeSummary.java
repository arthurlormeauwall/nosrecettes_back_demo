package com.arwall.nosrecettes.domain.model;

import lombok.Getter;

@Getter
public class RecipeSummary {

    private final Long id;
    private final String name;
    private final String source;
    private final Season season;
    private final String type;

    private RecipeSummary(Builder builder) {
        this.name = builder.name;
        this.season = builder.season;
        this.type = builder.type;
        this.source = builder.source;
        this.id = builder.id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(RecipeSummary recipe) {
        return new Builder(recipe);
    }

    public static Builder builder(Recipe recipe) {
        return new Builder(recipe);
    }

    public static class Builder {

        private Long id;
        private String name;
        private String source;
        private Season season;
        private String type;

        public Builder() {
        }

        public Builder(RecipeSummary recipe) {
            this.name = recipe.getName();
            this.id = recipe.getId();
            this.type = recipe.getType();
            this.season = recipe.getSeason();
            this.source = recipe.getSource();
        }
        public Builder(Recipe recipe) {
            this.name = recipe.getName();
            this.id = recipe.getId();
            this.type = recipe.getType();
            this.season = recipe.getSeason();
            this.source = recipe.getSource();
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

        public RecipeSummary build() {
            return new RecipeSummary(this);
        }
    }
}
