package com.arwall.nosrecettes.infra;

import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.populateRecipeWithItemIds;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.pushRecipe;
import static com.arwall.nosrecettes.infra.apiutil.ShoppingListApiUtil.deleteAllIngredientFromMenus;
import static com.arwall.nosrecettes.infra.apiutil.ShoppingListApiUtil.deleteAllIngredientFromShoppingList;
import static com.arwall.nosrecettes.infra.util.FileUtil.FARINE_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.FRAISE_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.LAIT_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.PIZZA_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.PRUNE_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.RIZ_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.RIZ_SAUTE_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.TARTE_AUX_FRAISES_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.TARTE_AUX_PRUNES_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.TOMATE_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.getFileContent;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import com.arwall.nosrecettes.Application;
import com.arwall.nosrecettes.infra.apiutil.ItemApiUtil;
import com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil;
import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.rest.model.RestItem;
import com.arwall.nosrecettes.rest.model.RestRecipe;
import com.arwall.nosrecettes.rest.model.RestRecipeSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = Application.class)
@SpringBootConfiguration
public class AbstractIntegrationT {

    protected HttpClient httpclient;
    protected ObjectMapper mapper;
    protected String tarteAuxFraisesFile;
    protected String tarteAuxPrunesFile;
    protected String rizSauteFile;
    protected String pizzaFile;

    protected AbstractIntegrationT() throws IOException {
        mapper = new ObjectMapper();
        httpclient = HttpClients.createDefault();
        tarteAuxFraisesFile = getFileContent(TARTE_AUX_FRAISES_FILENAME);
        tarteAuxPrunesFile = getFileContent(TARTE_AUX_PRUNES_FILENAME);
        pizzaFile = getFileContent(PIZZA_FILENAME);
        rizSauteFile = getFileContent(RIZ_SAUTE_FILENAME);
    }

    protected void populateIngredientWithItemIds() throws IOException {
        var items = ItemApiUtil.getAllItems();
        tarteAuxFraisesFile = populateRecipeWithItemIds(tarteAuxFraisesFile,
                getItemId(items, "fraise"),
                getItemId(items, "farine"));
        tarteAuxPrunesFile = populateRecipeWithItemIds(tarteAuxPrunesFile,
                getItemId(items, "prune"),
                getItemId(items, "lait"));
        pizzaFile = populateRecipeWithItemIds(pizzaFile,
                getItemId(items, "tomate"),
                getItemId(items, "farine"));
        rizSauteFile = populateRecipeWithItemIds(rizSauteFile,
                getItemId(items, "riz"),
                getItemId(items, "tomate"));
    }

    private void populateRecipesWithIds() throws IOException {
        var recipes = RecipeApiUtil.getAllRecipe();
        tarteAuxFraisesFile = populateRecipeWithId(tarteAuxFraisesFile,
                getRecipeId(recipes, "tarte aux fraise"));
        tarteAuxPrunesFile = populateRecipeWithId(tarteAuxPrunesFile,
                getRecipeId(recipes, "tarte aux prunes"));
        pizzaFile = populateRecipeWithId(pizzaFile,
                getRecipeId(recipes, "pizza"));
        rizSauteFile = populateRecipeWithId(rizSauteFile,
                getRecipeId(recipes, "riz saute"));
    }

    protected String populateRecipeWithId(String file, Long id) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var object = mapper.readValue(file, RestRecipe.class);
        var newObject = new RestRecipe(Recipe.builder(object.toDomain())
                .withId(id)
                .build());
        return mapper.writeValueAsString(newObject);
    }

    private Long getRecipeId(List<RestRecipeSummary> recipes, String name) {
        return recipes.stream().filter(recipe -> recipe.getName().equals(name)).findAny().get().getId();
    }


    private void deleteAllItems() throws IOException {
        ItemApiUtil.deleteAllItems();
    }

    private void deleteAllRecipes() throws IOException {
        RecipeApiUtil.deleteAllRecipes();
    }

    protected void pushItemToDb() throws IOException {
        ItemApiUtil.pushItems(List.of(
                getFileContent(FRAISE_FILENAME),
                getFileContent(PRUNE_FILENAME),
                getFileContent(RIZ_FILENAME),
                getFileContent(FARINE_FILENAME),
                getFileContent(LAIT_FILENAME),
                getFileContent(TOMATE_FILENAME)
        ));
    }

    private Long getItemId(List<RestItem> items, String itemName) {
        return items.stream().filter(i -> i.getName().equals(itemName)).findFirst().get().getId();
    }

    public void initDataBase() throws IOException {
        clearDataBase();
        pushItemToDb();
        populateIngredientWithItemIds();
        populateDbWithRecipes();
        populateRecipesWithIds();
    }


    private void populateDbWithRecipes() throws IOException {
        pushRecipe(tarteAuxFraisesFile);
        pushRecipe(tarteAuxPrunesFile);
        pushRecipe(pizzaFile);
        pushRecipe(rizSauteFile);
    }

    protected void clearDataBase() throws IOException {
        deleteAllIngredientFromMenus();
        deleteAllIngredientFromShoppingList();
        deleteAllRecipes();
        deleteAllItems();
    }
}
