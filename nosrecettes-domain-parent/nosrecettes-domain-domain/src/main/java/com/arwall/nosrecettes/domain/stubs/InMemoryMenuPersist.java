package com.arwall.nosrecettes.domain.stubs;

import java.util.ArrayList;
import java.util.List;

import com.arwall.nosrecettes.domain.model.Menu;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistMenu;

public class InMemoryMenuPersist implements PersistMenu {

    List<Long> menuRepository;

    public InMemoryMenuPersist() {
        menuRepository = new ArrayList<>();
    }

    @Override
    public List<Long> getMenu() {
        return menuRepository;
    }

    @Override
    public void addARecipeFromMenu(Long recipeId) {
        menuRepository.add(recipeId);
    }

    @Override
    public void removeRecipeFromMenu(Long id) {
        menuRepository.remove(id);
    }

    @Override
    public List<Menu> findAll() {
        return List.of(Menu.builder().withId(0L).withRecipeIds(menuRepository).build());
    }

    @Override
    public void update(Menu newEmptyMenu) {
        this.menuRepository= newEmptyMenu.getRecipeIds();
    }
}
