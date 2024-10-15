package com.arwall.nosrecettes;

import static com.arwall.nosrecettes.importInDb.ImportCsv.import_recipes;
import static com.arwall.nosrecettes.infra.apiutil.MenuApiUtil.menuEndpoint;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.pushRecipe;
import static com.arwall.nosrecettes.infra.apiutil.ShoppingListApiUtil.shoppingListEndpoint;
import static com.arwall.nosrecettes.infra.util.FileUtil.getFileContent;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

public class ApiTesting {

    @Test
    void should_test() throws Exception {
        import_recipes("importExportTest/nosrecettes.csv");
        add_to_menu();
    }

    @Test
    void add_to_menu() throws IOException {
        var addRecipes = new HttpPost(menuEndpoint + getString(List.of(24, 31, 64,75)));
        HttpClients.createDefault().execute(addRecipes);
    }

    @Test
    void update_ingredient_from_menu() throws IOException {
        var updateIngredientpost = new HttpPost(shoppingListEndpoint + "/frommenu");
        updateIngredientpost.setHeader("Content-type", "application/json");
        updateIngredientpost.setEntity(
                new StringEntity(getFileContent("ingredientFromMenu/ingfm23.json"), ContentType.APPLICATION_JSON));
        HttpClients.createDefault().execute(updateIngredientpost);
    }

    @Test
    void push_recipe() throws IOException {
        pushRecipe(getFileContent("recipes/zuc.json"));
    }

    @Test
    void force_sync_recipe() throws IOException {
        var httpclient = HttpClients.createDefault();
        var updatePost = new HttpPost(menuEndpoint + "/forcesync?+" + getString(List.of(13)));
        updatePost.setHeader("Content-type", "application/json");
        httpclient.execute(updatePost);
    }

    @Test
    void remove_from_menu() throws IOException {
        var addRecipes = new HttpPost(menuEndpoint + "/remove" + getString(List.of(13)));
        HttpClients.createDefault().execute(addRecipes);
    }

    private static String getString(List<Integer> ids) {
        var idsWord = ids.size() == 1 ? "id" : "ids";
        var s = "?" + idsWord + "=" + ids.get(0);
        for (int i = 1; i < ids.size(); i++) {
            s = s.concat("&" + idsWord + "=" + ids.get(i));
        }
        return s;
    }
}
