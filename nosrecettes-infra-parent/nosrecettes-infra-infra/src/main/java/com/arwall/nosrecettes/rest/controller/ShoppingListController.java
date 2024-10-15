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


import com.arwall.nosrecettes.domain.gatesToInfra.api.GetIngredientFromMenuSummary;
import com.arwall.nosrecettes.domain.gatesToInfra.api.IngredientFromMenuCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.IngredientFromShoppingListCrudOperations;
import com.arwall.nosrecettes.rest.model.RestFromMenuSummary;
import com.arwall.nosrecettes.rest.model.RestIngredientFromMenu;
import com.arwall.nosrecettes.rest.model.RestIngredientFromShoppingList;

@RestController
@CrossOrigin
@RequestMapping("/shoppinglist")
public class ShoppingListController {

    private final IngredientFromShoppingListCrudOperations ingredientFromShoppingListCrudOperations;

    private final IngredientFromMenuCrudOperations ingredientFromMenuCrudOperations;

    private final GetIngredientFromMenuSummary getIngredientFromMenuSummary;

    @Autowired
    public ShoppingListController (
            IngredientFromShoppingListCrudOperations ingredientFromShoppingListCrudOperations,
            IngredientFromMenuCrudOperations ingredientFromMenuCrudOperations,
            GetIngredientFromMenuSummary getIngredientFromMenuSummary
    ){
        this.ingredientFromShoppingListCrudOperations=ingredientFromShoppingListCrudOperations;
        this.ingredientFromMenuCrudOperations = ingredientFromMenuCrudOperations;
        this.getIngredientFromMenuSummary = getIngredientFromMenuSummary;

    }

    @GetMapping("/frommenu")
    public List<RestIngredientFromMenu> getIngredientsFromMenu() {
        return ingredientFromMenuCrudOperations.findAll().stream()
                .map(ingredientFromMenu -> new RestIngredientFromMenu(ingredientFromMenu))
                .toList();
    }

    @PostMapping("/frommenu/delete")
    public void deleteIngredientFromMenu (@RequestParam Long id) {
        ingredientFromMenuCrudOperations.delete(id);
    }

    @GetMapping("/summary")
    public RestFromMenuSummary getIngredientsFromMenuSummary() {
        return new RestFromMenuSummary(getIngredientFromMenuSummary.getIngredientFromMenuSummary());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/frommenu")
    public void ingredientFromMenuCrudOperations(@RequestBody RestIngredientFromMenu ingredient) {
        ingredientFromMenuCrudOperations.update(ingredient.toDomain());
    }

    @GetMapping
    public List<RestIngredientFromShoppingList> findAll() {
        return ingredientFromShoppingListCrudOperations.findAll().stream().map(ingredient -> new RestIngredientFromShoppingList(ingredient))
                .toList();
    }

    @GetMapping("/{id}")
    public RestIngredientFromShoppingList findById(@PathVariable Long id) throws Exception {
        return new RestIngredientFromShoppingList(ingredientFromShoppingListCrudOperations.findById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public RestIngredientFromShoppingList update(@RequestBody RestIngredientFromShoppingList ingredient) {
        return new RestIngredientFromShoppingList(ingredientFromShoppingListCrudOperations.update(ingredient.toDomain()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public void delete(@RequestParam long id) {
        ingredientFromShoppingListCrudOperations.delete(id);
    }
}
