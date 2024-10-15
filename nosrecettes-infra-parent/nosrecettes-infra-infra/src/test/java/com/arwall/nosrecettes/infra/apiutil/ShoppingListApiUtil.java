package com.arwall.nosrecettes.infra.apiutil;

import static com.arwall.nosrecettes.infra.apiutil.CommonUtil.getObjectFromEntiy;
import static com.arwall.nosrecettes.infra.util.HttpUtils.url;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.arwall.nosrecettes.rest.model.RestIngredientFromMenu;
import com.arwall.nosrecettes.rest.model.RestIngredientFromShoppingList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShoppingListApiUtil {

    public final static String shoppingListEndpoint = url() + "/shoppinglist";

    public static List<RestIngredientFromMenu> getRestIngredientFromMenus() throws IOException {
        var ingredientsFromMenuGet = new HttpGet(shoppingListEndpoint + "/frommenu");
        var httpclient = HttpClients.createDefault();
        var response = httpclient.execute(ingredientsFromMenuGet).getEntity();
        var actualIngredientFromMenu = new ObjectMapper().readValue(EntityUtils.toString(response, "UTF-8"),
                new TypeReference<List<RestIngredientFromMenu>>() {
                });
        return actualIngredientFromMenu;
    }

    public static RestIngredientFromShoppingList pushShoppingListIngredient() throws IOException {
        return pushShoppingListIngredient(new RestIngredientFromShoppingList());
    }

    public static RestIngredientFromShoppingList pushShoppingListIngredient(RestIngredientFromShoppingList ingredient)
            throws IOException {
        var httpclient = HttpClients.createDefault();
        var createPost = new HttpPost(shoppingListEndpoint);
        createPost.setHeader("Content-type", "application/json");
        createPost.setEntity(new StringEntity(new ObjectMapper()
                .writeValueAsString(ingredient),
                ContentType.APPLICATION_JSON));
        var entity = httpclient.execute(createPost).getEntity();
        return getObjectFromEntiy(entity, RestIngredientFromShoppingList.class);
    }


    public static List<RestIngredientFromShoppingList> retrieveAllIngredientFromShoppingList() throws IOException {
        var httpclient = HttpClients.createDefault();
        var httpGet = new HttpGet(shoppingListEndpoint);
        var getResponse = httpclient.execute(httpGet).getEntity();
        return new ObjectMapper().readValue(
                EntityUtils.toString(getResponse, "UTF-8"),
                new TypeReference<>() {
                });
    }
    public static List<RestIngredientFromMenu> retrieveAllIngredientFromMenu() throws IOException {
        var httpclient = HttpClients.createDefault();
        var httpGet = new HttpGet(shoppingListEndpoint+"/frommenu");
        var getResponse = httpclient.execute(httpGet).getEntity();
        return new ObjectMapper().readValue(
                EntityUtils.toString(getResponse, "UTF-8"),
                new TypeReference<>() {
                });
    }
    private static void deleteAnIngredientFromShoppingList(Long id) throws IOException {
        var deletePost = new HttpPost(shoppingListEndpoint + "/delete" + "?id=" + id);
        HttpClients.createDefault().execute(deletePost);
    }

    private static void deleteAnIngredientFromMenu(Long id) throws IOException {
        var deletePost = new HttpPost(shoppingListEndpoint +"/frommenu"+ "/delete" + "?id=" + id);
        HttpClients.createDefault().execute(deletePost);
    }
    public static void deleteAllIngredientFromShoppingList() throws IOException {
        var ids = retrieveAllIngredientFromShoppingList().stream().map(RestIngredientFromShoppingList::getId).toList();
        for (Long id : ids) {
            deleteAnIngredientFromShoppingList(id);
        }
    }

    public static void deleteAllIngredientFromMenus() throws IOException {
        var ids = retrieveAllIngredientFromMenu().stream().map(RestIngredientFromMenu::getId).toList();
        for (Long id : ids) {
            deleteAnIngredientFromMenu(id);
        }
    }
}
