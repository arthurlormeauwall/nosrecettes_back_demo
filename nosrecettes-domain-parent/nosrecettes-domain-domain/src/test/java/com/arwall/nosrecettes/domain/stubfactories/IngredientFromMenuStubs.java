package com.arwall.nosrecettes.domain.stubfactories;

import static com.arwall.nosrecettes.domain.stubfactories.ItemStubs.AN_ITEM_ID;
import static com.arwall.nosrecettes.domain.stubfactories.RecipeStubs.A_RECIPE_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.arwall.nosrecettes.domain.model.IngredientFromMenu;

public class IngredientFromMenuStubs {

    public static final Float A_QUANTITY = 1F;
    public static final Long AN_INGREDIENT_ID = 3L;

    public static final IngredientFromMenu AN_INGREDIENT_FROM_MENU = getIngredientFromMenu();

    private static IngredientFromMenu getIngredientFromMenu() {
        var quantities= new HashMap<Long, Float>();
        quantities.put(A_RECIPE_ID, A_QUANTITY);
        return IngredientFromMenu.builder()
                .withItemId(AN_ITEM_ID)
                .withQuanties(quantities)
                .withId(AN_INGREDIENT_ID)
                .withRecipeId(Set.of(A_RECIPE_ID))
                .build();
    }

    public static final Long A_NEW_INGREDIENT_FROM_MENU_ID = 8L;
    public static final IngredientFromMenu A_NEW_INGREDIENT_FROM_MENU = IngredientFromMenu.builder()
            .withId(A_NEW_INGREDIENT_FROM_MENU_ID)
            .build();
}
