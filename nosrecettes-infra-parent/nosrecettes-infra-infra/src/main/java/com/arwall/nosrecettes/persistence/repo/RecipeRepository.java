package com.arwall.nosrecettes.persistence.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.arwall.nosrecettes.persistence.model.RecipeData;


// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface RecipeRepository extends JpaRepository<RecipeData, Long> {
}