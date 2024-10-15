package com.arwall.nosrecettes.persistence;


import static java.util.Collections.emptyList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arwall.nosrecettes.domain.model.Menu;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistMenu;
import com.arwall.nosrecettes.persistence.model.MenuData;
import com.arwall.nosrecettes.persistence.repo.MenuRepository;

@Service
public class MenuPersist implements PersistMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuPersist.class);

    @Autowired
    MenuRepository menuRepository;

    @Override
    public List<Long> getMenu() {
        var menu = menuRepository.findAll();
        if (menu.isEmpty()) {
            var newEmptyMenu = new MenuData();
            menuRepository.save(newEmptyMenu);
            LOGGER.warn("Trying to get recipes from menu but there is no menu ; "
                    + "create a menu");
            return emptyList();
        } else {
            return menu.get(0).getRecipesId();
        }
    }

    @Override
    public void addARecipeFromMenu(Long recipeId) {
        var menus = menuRepository.findAll();
        if (menus.isEmpty()) {
            var newEmptyMenu = new MenuData();
            menuRepository.save(newEmptyMenu);
            LOGGER.warn("Trying to add a recipe to menu but there is no menu ; "
                    + "create a new menu");
            menus = menuRepository.findAll();
        }
        var menu = menus.getFirst();
        menu.getRecipesId().add(recipeId);
        menuRepository.save(menu);
    }

    @Override
    public void removeRecipeFromMenu(Long recipeId) {
        var menus = menuRepository.findAll();
        if (menus.isEmpty()) {
            var newEmptyMenu = new MenuData();
            menuRepository.save(newEmptyMenu);
            LOGGER.warn("Trying to remove a recipe from menu but there is no menu ; "
                    + "create a menu and do nothing");
        } else {
            var menu = menus.getFirst();
            menu.getRecipesId().remove(recipeId);
            menuRepository.save(menu);
        }
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll().stream().map(menuData -> Menu.builder(menuData.toDomain()).build()).toList();
    }

    @Override
    public void update(Menu menu) {
        menuRepository.save(new MenuData(menu));
    }
}