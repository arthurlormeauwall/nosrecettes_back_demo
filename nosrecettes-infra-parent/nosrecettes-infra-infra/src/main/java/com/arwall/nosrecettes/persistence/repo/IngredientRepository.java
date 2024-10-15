package com.arwall.nosrecettes.persistence.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arwall.nosrecettes.persistence.model.IngredientData;
import com.arwall.nosrecettes.persistence.model.RecipeData;


// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface IngredientRepository extends JpaRepository<IngredientData, Long> {
    List<IngredientData> findByRecipeId(Long recipeId);
}