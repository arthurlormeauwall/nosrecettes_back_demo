package com.arwall.nosrecettes.persistence.model;

import com.arwall.nosrecettes.domain.model.IngredientFromMenu;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


@Entity
@Getter
@Setter
public class IngredientFromMenuData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    private List<Long> recipeIds;

    @ElementCollection
    private List<Float> quantities;

    private Long itemId;

    private Float rectification;

    public IngredientFromMenuData() {
    }

    public IngredientFromMenuData(IngredientFromMenu ingredient) {
        this.quantities= new ArrayList<>();
        this.recipeIds = new ArrayList<>();
        for(Long id : ingredient.getRecipeId()){
            this.quantities.add(ingredient.getQuantitiesPerRecipes().get(id));
        }
        for(Long id : ingredient.getRecipeId()){
            this.recipeIds.add(id);
        }
        this.itemId = ingredient.getItemId();
        this.id = ingredient.getId();
        this.rectification=ingredient.getRectification();
    }

    public IngredientFromMenu toDomain() {
        var newQuantitiesMap =new HashMap();
        var newRecipeIds = new HashSet();
        for(int i= 0; i<this.quantities.size();i++){
            newQuantitiesMap.put(this.recipeIds.get(i), this.quantities.get(i));
        }
        for(int i= 0; i<this.recipeIds.size();i++){
            newRecipeIds.add(this.recipeIds.get(i));
        }
        return IngredientFromMenu.builder()
                .withQuanties(newQuantitiesMap)
                .withItemId(this.itemId)
                .withId(this.id)
                .withRecipeId(newRecipeIds)
                .withRectification(this.rectification)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientFromMenuData)) {
            return false;
        }
        return id != null && id.equals(((IngredientFromMenuData) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

