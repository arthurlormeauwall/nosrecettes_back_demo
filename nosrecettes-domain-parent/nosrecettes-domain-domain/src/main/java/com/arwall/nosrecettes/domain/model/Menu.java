package com.arwall.nosrecettes.domain.model;

import java.util.List;

import lombok.Getter;

@Getter
public class Menu {

    private final Long id;
    private final List<Long> recipeIds;

    public Menu(Builder builder) {
        this.id = builder.id;
        this.recipeIds = builder.recipeIds;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Menu recipe) {
        return new Builder(recipe);
    }

    public static class Builder {

        private Long id;
        private List<Long> recipeIds;

        public Builder(Menu menu) {
            this.id = menu.id;
            this.recipeIds = menu.recipeIds;
        }

        public Builder() {
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withRecipeIds(List<Long> recipeIds) {
            this.recipeIds = recipeIds;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }

    }

}
