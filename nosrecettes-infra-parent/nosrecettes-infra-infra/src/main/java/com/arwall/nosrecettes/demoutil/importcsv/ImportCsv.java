package com.arwall.nosrecettes.demoutil.importcsv;

import com.arwall.nosrecettes.rest.controller.ItemController;
import com.arwall.nosrecettes.rest.controller.RecipeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class ImportCsv {

    public static final String TYPE_MARKER = "_@@TYPE ";

    RecipeController recipeController;
    ItemController itemController;
    ImportRecipeUtil importRecipeUtil;
    ImportItemUtil importItemUtil;


    @Autowired
    ImportCsv(RecipeController recipeController,
              ItemController itemController,
              ImportRecipeUtil importRecipeUtil,
              ImportItemUtil importItemUtil) {
        this.recipeController = recipeController;
        this.itemController = itemController;
        this.importRecipeUtil = importRecipeUtil;
        this.importItemUtil = importItemUtil;
    }

    public void import_recipes() throws Exception {
        var listOfRecipes = OpenCsvUtil.readLineByLine("demotutil/toImport/nosrecettes.csv");
        import_items(listOfRecipes.getFirst());
        var items = itemController.findAll();
        for (int i = 1; i < listOfRecipes.size(); i++) {
            var recipe = importRecipeUtil.getRestRecipe(listOfRecipes.getFirst(), listOfRecipes.get(i), items);
            recipeController.update(recipe);
        }
    }

    private void import_items(String[] itemArray) {
        var itemList = Arrays.stream(itemArray).toList();
        itemList = itemList.subList(3, itemList.size());
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
                importItemUtil.importItem(previewsItemToImport, itemToImport, nextItemToImport, type);
            }
        }
    }
}
