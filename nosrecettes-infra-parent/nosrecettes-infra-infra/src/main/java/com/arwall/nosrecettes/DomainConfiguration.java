package com.arwall.nosrecettes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arwall.nosrecettes.domain.gatesToInfra.api.GetIngredientFromMenuSummary;
import com.arwall.nosrecettes.domain.gatesToInfra.api.IngredientFromMenuCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.ItemCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.ManageMenuRecipes;
import com.arwall.nosrecettes.domain.gatesToInfra.api.MenuCrudOperations;
import com.arwall.nosrecettes.domain.gatesToInfra.api.RecipeCrudOperations;
import com.arwall.nosrecettes.domain.service.ItemService;
import com.arwall.nosrecettes.domain.service.MenuService;
import com.arwall.nosrecettes.domain.service.RecipeService;
import com.arwall.nosrecettes.domain.service.IngredientFromMenuService;
import com.arwall.nosrecettes.domain.service.ShoppingListService;
import com.arwall.nosrecettes.domain.service.ShoppingListSummaryService;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistIngredientFromMenu;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistIngredientFromShoppingList;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistItem;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistMenu;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistRecipe;

@Configuration
public class DomainConfiguration {

    @Bean
    public ItemCrudOperations itemService(PersistItem itemPersist) {
        return new ItemService(itemPersist);
    }

    @Bean
    public ManageMenuRecipes manageMenuRecipes(
            PersistMenu menuPersist,
            IngredientFromMenuService ingredientFromMenuService,
            RecipeCrudOperations recipeCrudOperations) {
        return new MenuService(menuPersist, ingredientFromMenuService, recipeCrudOperations);
    }

    @Bean
    public MenuCrudOperations menuCrudOperations(PersistMenu menuPersist,
            IngredientFromMenuService ingredientFromMenuService,
            RecipeCrudOperations recipeCrudOperations) {
        return new MenuService(menuPersist, ingredientFromMenuService, recipeCrudOperations);
    }

    @Bean
    public IngredientFromMenuService shoppingListFromMenuService(
            PersistIngredientFromMenu ingredientFromMenuPersistSpi) {
        return new IngredientFromMenuService(ingredientFromMenuPersistSpi);
    }

    @Bean
    public RecipeCrudOperations recipeCrudOperations(
            PersistRecipe persistRecipe
    ) {
        return new RecipeService(persistRecipe);
    }

    @Bean
    public ShoppingListService shoppingListService(
            PersistIngredientFromShoppingList ingredientShoppingListPersistSpi) {
        return new ShoppingListService(ingredientShoppingListPersistSpi);
    }

    @Bean
    public GetIngredientFromMenuSummary getIngredientFromMenuSummary(
            MenuCrudOperations menuCrudOperations,
            RecipeCrudOperations recipeCrudOperations,
            ItemCrudOperations itemCrudOperations,
            IngredientFromMenuCrudOperations ingredientFromMenuCrudOperations
    ) {
        return new ShoppingListSummaryService(menuCrudOperations, recipeCrudOperations, itemCrudOperations, ingredientFromMenuCrudOperations);
    }
}
