package com.arwall.nosrecettes.domain.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class FromMenuSummary {

    private final List<IngredientFromMenuSummary> shoppingList;
    private final Map<String, List<IngredientFromMenuSummary>> shoppingListPerRecipe;

    private FromMenuSummary(Builder builder) {
        this.shoppingList = builder.shoppingList;
        this.shoppingListPerRecipe = builder.shoppingListPerRecipe;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(FromMenuSummary ingredient) {
        return new Builder(ingredient);
    }

    public static class Builder {

        private List<IngredientFromMenuSummary> shoppingList;
        private Map<String, List<IngredientFromMenuSummary>> shoppingListPerRecipe;

        public Builder() {
        }

        public Builder(FromMenuSummary fromMenuSummary) {
            this.shoppingList = fromMenuSummary.getShoppingList();
            this.shoppingListPerRecipe = fromMenuSummary.getShoppingListPerRecipe();
        }

        public Builder withShoppingList(List<IngredientFromMenuSummary> shoppingList) {
            this.shoppingList = shoppingList;
            return this;
        }

        public Builder withShoppingListPerRecipe(Map<String, List<IngredientFromMenuSummary>> shoppingListPerRecipe) {
            this.shoppingListPerRecipe = shoppingListPerRecipe;
            return this;
        }

        public FromMenuSummary build() {
            return new FromMenuSummary(this);
        }
    }
}

