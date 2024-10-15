package com.arwall.nosrecettes.importInDb;


import static com.arwall.nosrecettes.importInDb.ImportCsv.TYPE_MARKER;
import static com.arwall.nosrecettes.infra.apiutil.ItemApiUtil.getAllItems;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.getAllRecipe;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.retrieveRecipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import com.arwall.nosrecettes.Application;
import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.rest.model.RestItem;
import com.arwall.nosrecettes.rest.model.RestRecipe;
import com.arwall.nosrecettes.rest.model.RestRecipeSummary;

@SpringBootTest(classes = Application.class)
@SpringBootConfiguration
public class ExportCsv {

    @Test
    public static void exportCsv(String fileName) throws Exception {
        OpenCsvUtil.writeAllLines(getLines(),
                fileName);
    }

    @Test
    public void exportCsv() throws Exception {
        OpenCsvUtil.writeAllLines(getLines(),
                "export.csv");
    }

    private static List<String[]> getLines() throws IOException {
        var lines = new ArrayList<String[]>();
        var allItems = getAllItems().stream().map(RestItem::toDomain).toList();
        String[] firstLine = getFirstLine(allItems);
        lines.add(firstLine);

        var itemsFromFirstLine = getItemAsList(firstLine);
        var recipes = getAllRecipe();

        recipes.forEach(recipe -> {
            String[] line;
            try {
                line = getLine(recipe, itemsFromFirstLine, allItems);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lines.add(line);
        });
        return lines;
    }

    private static List<String> getItemAsList(String[] firstLine) {
        return Arrays.stream(firstLine).toList().subList(3, firstLine.length);
    }

    private static String[] getLine(RestRecipeSummary recipe, List<String> itemsFromFirstLine, List<Item> items)
            throws IOException {
        var fullRecipe = retrieveRecipe(recipe.getId());
        var recipeLine = new ArrayList<String>();
        recipeLine.add(recipe.getName());
        recipeLine.add(recipe.getType());
        recipeLine.add(recipe.getSource());
        itemsFromFirstLine.forEach(itemName -> {
            String ingredient = resolveIngredientQuantity(itemName, fullRecipe, items);
            recipeLine.add(ingredient);
        });
        return recipeLine.toArray(new String[0]);
    }

    private static String resolveIngredientQuantity(String itemName, RestRecipe fullRecipe, List<Item> items) {
        return fullRecipe.getIngredients().stream()
                .filter(ingredient -> {
                    if (itemName.contains(TYPE_MARKER)) {
                        return false;
                    }
                    Optional<Item> itemOptional = items.stream().filter(it -> {
                                String name = it.getName();
                                if (!name.contains("QTE") && !name.contains("GR") && !name.contains("ML")) {
                                    String[] split = itemName.split(" _");
                                    var itemName2 = split[0];
                                    return name.equals(itemName2);
                                } else {
                                    String[] split = itemName.split(" _");
                                    var itemName2 = split[0] + " " + split[1];
                                    return name.equals(itemName2);
                                }
                            })
                            .findAny();
                    var item = itemOptional.get();
                    return ingredient.getItemId().equals(item.getId());
                })
                .findAny()
                .map(ingredient -> String.valueOf(ingredient.getQuantity()))
                .orElse("");
    }

    private static String[] getFirstLine(List<Item> allItems) {
        var firstLine = new ArrayList<String>();
        firstLine.add("Nom");
        firstLine.add("Type");
        firstLine.add("Lien / livre et page");
        for (int i = 0; i < allItems.size(); i++) {
            var previousItem = getPreviousItem(allItems, i);
            var item = allItems.get(i);
            if (previousItem == null || !item.getType().equals(previousItem.getType())) {
                firstLine.add(TYPE_MARKER + item.getType());
            }
            String itemName = item.getName();
            if (itemName.contains(" GR")) {
                itemName = itemName.split(" GR")[0];
            }
            if (itemName.contains(" QTE")) {
                itemName = itemName.split(" QTE")[0];
            }
            if (itemName.contains(" ML")) {
                itemName = itemName.split(" ML")[0];
            }
            firstLine.add(itemName + " _" + resolveType(item));

        }
        return firstLine.toArray(new String[0]);
    }

    private static Item getPreviousItem(List<Item> allItems, int i) {
        Item previousItem;
        if (i == 0) {
            previousItem = null;
        } else {
            previousItem = allItems.get(i - 1);
        }
        return previousItem;
    }

    private static String resolveType(Item item) {
        switch (item.getQuantityType()) {
            case AMOUNT:
                return "QTE";
            case GR:
                return "GR";
            case ML:
                return "ML";
        }
        return null;
    }
}
