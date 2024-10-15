package com.arwall.nosrecettes.infra.apiutil;

import static com.arwall.nosrecettes.infra.apiutil.ShoppingListApiUtil.shoppingListEndpoint;
import static com.arwall.nosrecettes.infra.util.HttpUtils.url;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.arwall.nosrecettes.rest.model.RestIngredientFromMenu;
import com.arwall.nosrecettes.rest.model.RestRecipeSummary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MenuApiUtil {

    public final static String menuEndpoint = url() + "/menu";

    public static CloseableHttpResponse addTwoRecipesToMenus(Long firstId, Long secondId) throws IOException {
        var addRecipes = new HttpPost(menuEndpoint + "?ids=" + firstId + "&ids=" + secondId);
        return HttpClients.createDefault().execute(addRecipes);
    }

    public static CloseableHttpResponse removeRecipeFromMenu(Long recipeId) throws IOException {
        var removeRecipes = new HttpPost(menuEndpoint + "/remove" + "?ids=" + recipeId);
        return HttpClients.createDefault().execute(removeRecipes); // removing tarte aux fraises
    }

    public static List<RestRecipeSummary> getRestRecipeSummaries() throws IOException {
        var getAll = new HttpGet(menuEndpoint);
        var menus = HttpClients.createDefault().execute(getAll);
        var menuRecipes = new ObjectMapper().readValue(EntityUtils.toString(menus.getEntity()),
                new TypeReference<List<RestRecipeSummary>>() {
                });
        return menuRecipes;
    }

    public static org.apache.http.client.methods.CloseableHttpResponse updateIngredientOfMenu(
            RestIngredientFromMenu newIngredient) throws IOException {
        var updatePost = new HttpPost(shoppingListEndpoint + "/frommenu" + "?id=" + newIngredient.getId());
        updatePost.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(newIngredient),
                ContentType.APPLICATION_JSON));
        return HttpClients.createDefault().execute(updatePost);
    }
}
