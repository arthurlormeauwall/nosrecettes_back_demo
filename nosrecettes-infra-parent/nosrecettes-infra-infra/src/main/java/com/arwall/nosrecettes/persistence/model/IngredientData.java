package com.arwall.nosrecettes.persistence.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.arwall.nosrecettes.domain.model.Ingredient;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class IngredientData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private RecipeData recipe;

    private float quantity;

    private Long itemId;

    public IngredientData() {
    }

    public IngredientData(Ingredient ingredient) {
        this.quantity = ingredient.getQuantity();
        this.itemId = ingredient.getItemId();
        this.id = ingredient.getId();
    }

    public Ingredient toDomain() {
        return Ingredient.builder()
                .withQuantiy(this.quantity)
                .withItemId(this.itemId)
                .withId(id)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientData)) {
            return false;
        }
        return id != null && id.equals(((IngredientData) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

