package com.arwall.nosrecettes.domain.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.arwall.nosrecettes.domain.ddd.DomainEntity;

@Getter
public class IngredientFromMenu extends DomainEntity {

    private final Long itemId;
    private final Map<Long, Float> quantitiesPerRecipes;
    private final Set<Long> recipeId;
    private final Float rectification;

    private IngredientFromMenu(Builder builder) {
        super(builder.id);
        this.quantitiesPerRecipes = builder.quantities;
        this.itemId = builder.itemId;
        this.recipeId = builder.recipeId;
        if(null!=builder.rectification)
            this.rectification=builder.rectification;
        else
            this.rectification=0F;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(IngredientFromMenu ingredient) {
        return new Builder(ingredient);
    }

    public Float getQuantity() {
        var it= quantitiesPerRecipes.entrySet().iterator();
        Float totalQuantity = 0F;
        while(it.hasNext()){
            totalQuantity += it.next().getValue();
        }
        return totalQuantity+rectification;
    }

    public static class Builder {

        public Set<Long> recipeId;
        private Map<Long, Float> quantities;
        private Long itemId;
        private Long id;
        private Float rectification;

        public Builder() {
            rectification=0F;
            recipeId=new HashSet<>();
            quantities= new HashMap<>();
        }

        public Builder(IngredientFromMenu ingredient) {
            this.id = ingredient.getId();
            this.itemId = ingredient.getItemId();
            this.quantities = ingredient.getQuantitiesPerRecipes();
            this.recipeId = ingredient.getRecipeId();
            this.rectification= ingredient.getRectification();
        }

        public Builder withQuanties(Map<Long, Float> quantity) {
            this.quantities = quantity;
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

        public Builder withRecipeId(Set<Long> id) {
            this.recipeId = id;
            return this;
        }

        public IngredientFromMenu build() {
            return new IngredientFromMenu(this);
        }

        public Builder withRectification(Float rectification) {
            this.rectification=rectification;
            return this;
        }
    }
}

