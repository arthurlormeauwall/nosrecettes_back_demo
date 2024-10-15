package com.arwall.nosrecettes.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arwall.nosrecettes.persistence.model.IngredientFromShoppingListData;

public interface IngredientFromShoppingListRepository extends
        JpaRepository<IngredientFromShoppingListData, Long> {

}
