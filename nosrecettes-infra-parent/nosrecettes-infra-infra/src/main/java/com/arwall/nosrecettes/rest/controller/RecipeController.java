package com.arwall.nosrecettes.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arwall.nosrecettes.domain.gatesToInfra.api.ManageMenuRecipes;
import com.arwall.nosrecettes.domain.gatesToInfra.api.MenuCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.RecipeCrudOperations;
import com.arwall.nosrecettes.rest.model.RestRecipe;
import com.arwall.nosrecettes.rest.model.RestRecipeSummary;

@RestController
@CrossOrigin
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeCrudOperations recipeCrudOperations;
    private final MenuCrudOperations menuCrudOperations;
    private final ManageMenuRecipes manageMenuRecipes;

    @Autowired
    public RecipeController(RecipeCrudOperations recipeCrudOperations,
            MenuCrudOperations menuCrudOperations,
            ManageMenuRecipes manageMenuRecipes
    ) {
        this.recipeCrudOperations = recipeCrudOperations;
        this.menuCrudOperations = menuCrudOperations;
        this.manageMenuRecipes = manageMenuRecipes;
    }

    @GetMapping
    public List<RestRecipeSummary> findAll() {
        return recipeCrudOperations.findAll().stream().map(recipe -> new RestRecipeSummary(recipe)).toList();
    }

    @GetMapping("/{id}")
    public RestRecipe retrieve(@PathVariable Long id) throws Exception {
        return new RestRecipe(recipeCrudOperations.retrieve(id));

    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public RestRecipe update(@RequestBody RestRecipe restRecipe) {
        return new RestRecipe(recipeCrudOperations.update(restRecipe.toDomain()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public void delete(@RequestParam long id) {
        if (manageMenuRecipes.isThisRecipeInMenu(id)) {
            manageMenuRecipes.removeRecipeFromMenu(id);
        }
        recipeCrudOperations.delete(id);
    }
}
