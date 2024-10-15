package com.arwall.nosrecettes.domain.gatesToInfra.api;

import java.util.Arrays;
import java.util.List;

import com.arwall.nosrecettes.domain.model.IngredientFromMenu;
import com.arwall.nosrecettes.domain.model.IngredientFromShoppingList;

public interface IngredientFromMenuCrudOperations {


    IngredientFromMenu retrieve(Long id);

    IngredientFromMenu update(IngredientFromMenu ingredient);

    void delete(Long id);

    List<IngredientFromMenu> findAll();
}
