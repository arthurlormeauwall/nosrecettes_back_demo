package com.arwall.nosrecettes.persistence.model;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.RecipeSummary;
import com.arwall.nosrecettes.domain.model.Season;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RecipeData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String source;
    private Season season;

    private String type;

    @OneToMany(
            mappedBy = "recipe",
            cascade = ALL,
            fetch=LAZY,
            targetEntity = IngredientData.class,
            orphanRemoval = true
    )
    private List<IngredientData> ingredients;

    public RecipeData() {
    }

    public RecipeData(Recipe recipe) {
        this.name = recipe.getName();
        var ingredientsData = Optional.ofNullable(recipe.getIngredients())
                .map(ingredients -> ingredients.stream()
                        .map(ingredient -> new IngredientData(ingredient))
                        .toList())
                .orElse(null);
        this.ingredients = new ArrayList<>();
        if (null != ingredientsData) {
            for (IngredientData ingredientData : ingredientsData) {
                addIngredient(ingredientData);
            }
        }
        this.season = recipe.getSeason();
        this.type = recipe.getType();
        this.source = recipe.getSource();
        this.id = recipe.getId();
    }

    public Recipe toDomain() {
        return Recipe.builder()
                .withName(this.name)
                .withSeason(this.season)
                .withIngredients(Optional.ofNullable(ingredients)
                        .map(ingredients -> ingredients.stream().map(ingredient -> ingredient.toDomain()).toList())
                        .orElse(null))
                .withSource(this.source)
                .withType(this.type)
                .withId(this.id)
                .build();
    }

    public void addIngredient(IngredientData ingredientData) {
        ingredients.add(ingredientData);
        ingredientData.setRecipe(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecipeData that)) {
            return false;
        }
        return id == that.id &&
                Objects.equals(name, that.name) && Objects.equals(source, that.source)
                && season == that.season && Objects.equals(type, that.type) && Objects.equals(
                ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, source, season, type, ingredients);
    }

    public RecipeSummary toDomainRecipeSummary() {
        return RecipeSummary.builder()
                .withName(this.name)
                .withSeason(this.season)
                .withSource(this.source)
                .withType(this.type)
                .withId(this.id)
                .build();
    }
    public void addIngredientData(IngredientData ingredientData) {
        getIngredients().add(ingredientData);
        ingredientData.setRecipe(this);
    }

    public void removeVariant(IngredientData ingredientData) {
        ingredientData.setRecipe(null);
        this.getIngredients().remove(ingredientData);
    }
}
