package com.arwall.nosrecettes.importInDb;


import static com.arwall.nosrecettes.infra.apiutil.ItemApiUtil.getAllItems;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.addRecipe;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import com.arwall.nosrecettes.Application;

@SpringBootTest(classes = Application.class)
@SpringBootConfiguration
public class ImportCsv {

    public static final String TYPE_MARKER = "_@@TYPE ";

    @Test
    public static void import_recipes(String fileName) throws Exception {
        Path filePath = Path.of(
                OpenCsvUtil.class.getClassLoader().getResource(fileName).getPath());
        var listOfRecipes = OpenCsvUtil.readLineByLine(filePath);
        import_items(listOfRecipes.get(0));
        var items = getAllItems();
        for (int i = 1; i < listOfRecipes.size(); i++) {
            var recipe = ImportRecipeUtil.getRestRecipe(listOfRecipes.get(0), listOfRecipes.get(i), items);
            addRecipe(recipe);
        }

    }

    @Test
    public void import_recipes() throws Exception {
        Path filePath = Path.of(
                OpenCsvUtil.class.getClassLoader().getResource("importExportTest/nosrecettes_ti.csv").getPath());
        var listOfRecipes = OpenCsvUtil.readLineByLine(filePath);
        import_items(listOfRecipes.get(0));
        var items = getAllItems();
        for (int i = 1; i < listOfRecipes.size(); i++) {
            var recipe = ImportRecipeUtil.getRestRecipe(listOfRecipes.get(0), listOfRecipes.get(i), items);
            addRecipe(recipe);
        }
    }

    @Test
    static void import_items(String[] itemArray) throws IOException {
        var itemList = Arrays.stream(itemArray).toList();
        itemList = itemList.subList(3,itemList.size());
        int lastItemIndex = itemList.size() - 1;

        var type = "default";
        String previewsItemToImport;
        String itemToImport;
        String nextItemToImport;
        for (int itemIndex = 0; itemIndex < itemList.size(); itemIndex++) {
            itemToImport = itemList.get(itemIndex);
            if (itemToImport.contains(TYPE_MARKER)) {
                type = itemToImport.split(TYPE_MARKER)[1];
            } else {
                if (itemIndex == 0) { // first index case
                    previewsItemToImport = null;
                    nextItemToImport = itemList.get(ImportItemUtil.nextIndex(itemIndex));
                } else if (itemIndex == lastItemIndex) { // last index case
                    previewsItemToImport = itemList.get(ImportItemUtil.previewsIndex(itemIndex));
                    nextItemToImport = null;
                } else {
                    previewsItemToImport = itemList.get(ImportItemUtil.previewsIndex(itemIndex));
                    nextItemToImport = itemList.get(ImportItemUtil.nextIndex(itemIndex));
                }
                ImportItemUtil.importItem(previewsItemToImport, itemToImport, nextItemToImport, type);
            }

        }
    }
}
