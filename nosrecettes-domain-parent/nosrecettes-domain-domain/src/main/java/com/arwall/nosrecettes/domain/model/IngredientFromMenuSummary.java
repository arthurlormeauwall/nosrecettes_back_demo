package com.arwall.nosrecettes.domain.model;

import lombok.Getter;

@Getter
public class IngredientFromMenuSummary {

    private final float quantity;
    private final String name;

    private IngredientFromMenuSummary(Builder builder) {
        this.quantity = builder.quantity;
        this.name = builder.name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(IngredientFromMenuSummary ingredient) {
        return new Builder(ingredient);
    }

    public static class Builder {

        private float quantity;
        private String name;

        public Builder() {
        }

        public Builder(IngredientFromMenuSummary ingredientFromMenuSummary) {
            this.name = ingredientFromMenuSummary.getName();
            this.quantity = ingredientFromMenuSummary.getQuantity();
        }

        public Builder withQuantiy(Float quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public IngredientFromMenuSummary build() {
            return new IngredientFromMenuSummary(this);
        }
    }
}

