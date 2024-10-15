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

import com.arwall.nosrecettes.domain.model.Ingredient;
import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.rest.model.RestRecipe;
import com.arwall.nosrecettes.rest.model.RestRecipeSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecipeApiUtil {

    public final static String recipeEndpoint = url() + "/recipe";


    public static RestRecipe addRecipe(RestRecipe recipe) throws IOException {
        var httpclient = HttpClients.createDefault();
        var createPost = new HttpPost(recipeEndpoint);
        createPost.setEntity(new StringEntity(new ObjectMapper()
                .writeValueAsString(recipe),
                ContentType.APPLICATION_JSON));
        var entity = httpclient.execute(createPost).getEntity();
        return getObjectFromEntiy(entity, RestRecipe.class);
    }

    public static void updateRecipe(long recipeId, String file) throws IOException {
        var httpclient = HttpClients.createDefault();
        var updatePost = new HttpPost(recipeEndpoint + "?id=" + recipeId);
        updatePost.setHeader("Content-type", "application/json");
        updatePost.setEntity(new StringEntity(file, ContentType.APPLICATION_JSON));
        httpclient.execute(updatePost);
    }

    public static void pushRecipe(String file) throws IOException {
        var httpclient = HttpClients.createDefault();
        var updatePost = new HttpPost(recipeEndpoint);
        updatePost.setHeader("Content-type", "application/json");
        updatePost.setEntity(new StringEntity(file, ContentType.APPLICATION_JSON));
        httpclient.execute(updatePost);
    }

    public static void deleteRecipe(Long id) throws IOException {
        var httpclient = HttpClients.createDefault();
        var updatePost = new HttpPost(recipeEndpoint + "/delete" + "?id=" + id);
        updatePost.setHeader("Content-type", "application/json");
        httpclient.execute(updatePost);
    }

    public static RestRecipe retrieveRecipe(Long id) throws IOException {
        var httpclient = HttpClients.createDefault();
        var httpGet = new HttpGet(recipeEndpoint + "/" + id);
        var getResponse = httpclient.execute(httpGet).getEntity();
        return new ObjectMapper().readValue(
                EntityUtils.toString(getResponse, "UTF-8"),
                RestRecipe.class);
    }

    public static List<RestRecipeSummary> getAllRecipe() throws IOException {
        var httpclient = HttpClients.createDefault();
        var updatePost = new HttpGet(recipeEndpoint);
        var response = httpclient.execute(updatePost).getEntity();
        return new ObjectMapper().readValue(
                EntityUtils.toString(response, "UTF-8"),
                new TypeReference<>() {
                });
    }

    public static void deleteAllRecipes() throws IOException {
        getAllRecipe().stream().forEach(recipe -> {
            try {
                deleteRecipe(recipe.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static String populateRecipeWithItemIds(String file,
            Long firstItemId, Long secondItemId) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var object = mapper.readValue(file, RestRecipe.class);
        object.getIngredients().get(0).setItemId(firstItemId);
        object.getIngredients().get(1).setItemId(secondItemId);
        return mapper.writeValueAsString(object);
    }

    public static Long getIdFromName(List<RestRecipeSummary> recipes, String name) {
        return recipes.stream().filter(r -> r.getName().equals(name)).findFirst().get().getId();
    }


    public static RestRecipe getRecipeWithNewNameAndIngredient(
            RestRecipe recipe,
            String newName,
            Ingredient firstIngredient,
            Ingredient secondIngredient) {
        var newTarteAuxPrunesRecipe = new RestRecipe(Recipe.builder(recipe.toDomain())
                .withName(newName)
                .withIngredients(List.of(
                        firstIngredient,
                        secondIngredient
                ))
                .build());
        return newTarteAuxPrunesRecipe;
    }


}
