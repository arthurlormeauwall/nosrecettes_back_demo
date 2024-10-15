package com.arwall.nosrecettes.domain.gatesToInfra.spi;

import java.util.List;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.IngredientFromMenu;

public interface PersistIngredientFromMenu {

    Optional<IngredientFromMenu> findById(Long id);

    List<IngredientFromMenu> retrieveAllIngredientFromMenu();

    IngredientFromMenu update(IngredientFromMenu Ingredient);
    IngredientFromMenu create();

    void delete(Long id);
}
