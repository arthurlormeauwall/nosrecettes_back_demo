package com.arwall.nosrecettes.domain.model;

import lombok.Getter;

@Getter
public class IngredientFromShoppingList {

    private final Long id;
    private final Long itemId;
    private final float quantity;

    private IngredientFromShoppingList(Builder builder) {
        this.quantity = builder.quantity;
        this.itemId = builder.itemId;
        this.id = builder.id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(IngredientFromShoppingList ingredient) {
        return new Builder(ingredient);
    }

    public static class Builder {

        private float quantity;
        private Long itemId;
        private Long id;

        public Builder() {
        }

        public Builder(IngredientFromShoppingList ingredient) {
            this.id = ingredient.getId();
            this.itemId = ingredient.getItemId();
            this.quantity = ingredient.getQuantity();
        }

        public Builder withQuantiy(Float quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withItemId(Long itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }


        public IngredientFromShoppingList build() {
            return new IngredientFromShoppingList(this);
        }
    }
}

