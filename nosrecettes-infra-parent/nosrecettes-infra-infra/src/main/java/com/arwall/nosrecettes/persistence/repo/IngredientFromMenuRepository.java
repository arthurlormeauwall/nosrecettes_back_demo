package com.arwall.nosrecettes.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arwall.nosrecettes.persistence.model.IngredientFromMenuData;

public interface IngredientFromMenuRepository extends JpaRepository<
        IngredientFromMenuData, Long> {

}
