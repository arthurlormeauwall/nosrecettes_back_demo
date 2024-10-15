package com.arwall.nosrecettes.persistence.model;

import com.arwall.nosrecettes.domain.model.IngredientFromShoppingList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class IngredientFromShoppingListData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private float quantity;

    private Long itemId;

    public IngredientFromShoppingListData() {
    }

    public IngredientFromShoppingListData(IngredientFromShoppingList ingredient) {
        this.quantity = ingredient.getQuantity();
        this.itemId = ingredient.getItemId();
        this.id = ingredient.getId();
    }

    public IngredientFromShoppingList toDomain() {
        return IngredientFromShoppingList.builder()
                .withQuantiy(this.quantity)
                .withItemId(this.itemId)
                .withId(this.id)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientFromShoppingListData)) {
            return false;
        }
        return id != null && id.equals(((IngredientFromShoppingListData) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

