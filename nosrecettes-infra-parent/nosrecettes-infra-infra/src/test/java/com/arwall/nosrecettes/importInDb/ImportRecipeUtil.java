package com.arwall.nosrecettes.importInDb;

import static com.arwall.nosrecettes.importInDb.ImportCsv.TYPE_MARKER;
import static com.arwall.nosrecettes.importInDb.ImportItemUtil.resolveQuantityType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.Ingredient;
import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.rest.model.RestItem;
import com.arwall.nosrecettes.rest.model.RestRecipe;

public class ImportRecipeUtil {

    public static RestRecipe getRestRecipe(String[] itemNames, String[] recipeAsArray, List<RestItem> items)
            throws IOException {
        return new RestRecipe(getRecipe(itemNames, recipeAsArray, items));
    }

    private static Recipe getRecipe(String[] itemNames, String[] recipeAsArray, List<RestItem> items)
            throws IOException {
        var recipe = Recipe.builder().withName(recipeAsArray[0]).withType(recipeAsArray[1]).withSource(recipeAsArray[2])
                .build();
        for (int ingredientIndex = 3; ingredientIndex < recipeAsArray.length; ingredientIndex++) {
            var quantityOfIngredient = recipeAsArray[ingredientIndex];
            if (!quantityOfIngredient.isEmpty()) {
                var nameOfIngredient = itemNames[ingredientIndex];
                if (!nameOfIngredient.contains(TYPE_MARKER)) {
                    var ingredient = getIngredient(nameOfIngredient, items, quantityOfIngredient);
                    recipe = addIngredient(recipe, ingredient);
                }
            }
        }
        return recipe;
    }

    private static Recipe addIngredient(Recipe recipe, Ingredient ingredient) {
        var ingredients = recipe.getIngredients();
        ingredients.add(ingredient);
        return Recipe.builder(recipe).withIngredients(ingredients).build();
    }

    private static Ingredient getIngredient(String nameAndType, List<RestItem> items, String quantity) {
        Long itemId = resolveItemId(nameAndType, items);

        return Ingredient.builder().withQuantiy(Float.valueOf(quantity)).withItemId(itemId).build();
    }

    private static Long resolveItemId(String nameAndType, List<RestItem> items) {
        var splitNameAndType = Arrays.stream(nameAndType.split(" _")).toList();
        var name = splitNameAndType.get(0);
        var type = splitNameAndType.get(1);

        var itemIds = items.stream().filter(item -> {
            String itemName = item.getName();
            if (itemName.contains("QTE")) {
                itemName = Arrays.stream(itemName.split(" QTE")).toList().getFirst();
            }
            if (itemName.contains("ML")) {
                itemName = Arrays.stream(itemName.split(" ML")).toList().getFirst();
            }
            if (itemName.contains("GR")) {
                itemName = Arrays.stream(itemName.split(" GR")).toList().getFirst();
            }
            return itemName.equals(name);
        }).toList();
        Long itemId;
        if (itemIds.size() > 1) {
            Optional<RestItem> any = itemIds.stream()
                    .filter(item -> item.getQuantityType().equals(resolveQuantityType(type)))
                    .findAny();
            itemId = any.get()
                    .getId();
        } else {
            itemId = itemIds.getFirst().getId();
        }
        return itemId;
    }
}
