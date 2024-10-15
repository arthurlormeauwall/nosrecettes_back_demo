package com.arwall.nosrecettes.rest.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arwall.nosrecettes.domain.gatesToInfra.api.ManageMenuRecipes;
import com.arwall.nosrecettes.domain.gatesToInfra.api.MenuCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.RecipeCrudOperations;
import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.rest.model.RestRecipeSummary;

@RestController
@CrossOrigin
@RequestMapping("/menu")
public class MenuController {

    private final ManageMenuRecipes manageMenuRecipes;
    private final MenuCrudOperations menuCrudOperations;
    private final RecipeCrudOperations recipeCrudOperations;

    @Autowired
    public MenuController(ManageMenuRecipes manageMenuRecipes,
            MenuCrudOperations menuCrudOperations,
            RecipeCrudOperations recipeCrudOperations) {
        this.manageMenuRecipes = manageMenuRecipes;
        this.menuCrudOperations = menuCrudOperations;
        this.recipeCrudOperations = recipeCrudOperations;
    }

    @GetMapping
    public List<RestRecipeSummary> getMenu() {
        List<Recipe> recipe = menuCrudOperations.getMenu().stream()
                .map(id -> recipeCrudOperations.retrieve(id)).toList();
        return recipe.stream()
                .map(Recipe::getSummary)
                .map(RestRecipeSummary::new).toList();
    }

    @PostMapping
    public void addARecipes(@RequestParam List<Long> ids) {
        for (Long id : ids) {
            manageMenuRecipes.addARecipeToMenu(id);
        }
    }

    @PostMapping("/remove")
    public void removeRecipes(@RequestParam List<Long> ids) throws Exception {
        for (Long id : ids) {
            manageMenuRecipes.removeRecipeFromMenu(id);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forcesync")
    public void forceSync(@RequestParam long id) {
        manageMenuRecipes.forceSync(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/hardforcesync")
    public void hardForceSync(@RequestParam Long id) {
        manageMenuRecipes.hardForceSync(id);
    }
}
