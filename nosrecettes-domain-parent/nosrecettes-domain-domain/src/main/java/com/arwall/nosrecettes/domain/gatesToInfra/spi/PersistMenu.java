package com.arwall.nosrecettes.domain.gatesToInfra.spi;

import java.util.List;

import com.arwall.nosrecettes.domain.model.Menu;

public interface PersistMenu {


    List<Long> getMenu();

    void addARecipeFromMenu(Long recipeId);

    void removeRecipeFromMenu(Long id);

    List<Menu> findAll();

    void update(Menu newEmptyMenu);
}
