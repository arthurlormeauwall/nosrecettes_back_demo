package com.arwall.nosrecettes.infra;

import static com.arwall.nosrecettes.infra.apiutil.CommonUtil.getObjectFromEntiy;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.addRecipe;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.getAllRecipe;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.getRecipeWithNewNameAndIngredient;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.recipeEndpoint;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.retrieveRecipe;
import static com.arwall.nosrecettes.infra.util.FileUtil.RECIPE_SUMMARY_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.getFileContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil;
import com.arwall.nosrecettes.domain.model.Ingredient;
import com.arwall.nosrecettes.rest.model.RestIngredient;
import com.arwall.nosrecettes.rest.model.RestRecipe;
import com.arwall.nosrecettes.rest.model.RestRecipeSummary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(value = "dev")
public class RecipeIT extends AbstractIntegrationT {


    RecipeIT() throws IOException {
    }

    public static Ingredient getIngredientWithNewQuantity(RestIngredient firstIngredient, float newQuantity) {
        return new RestIngredient(Ingredient.builder(firstIngredient.toDomain())
                .withQuantiy(newQuantity)
                .build()).toDomain();
    }

    @BeforeEach
    void setup() throws IOException {
    }

    @Test
    void should_create_new_recipe() throws IOException {
        // Given
        HttpPost createPost = new HttpPost(recipeEndpoint);
        createPost.setEntity(new StringEntity(new ObjectMapper()
                .writeValueAsString(new RestRecipe()),
                ContentType.APPLICATION_JSON));

        // When
        HttpResponse createResponse = httpclient.execute(createPost);

        // Then
        assertThat(createResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        HttpEntity entity = createResponse.getEntity();
        var actualRecipe = getObjectFromEntiy(entity, RestRecipe.class);
        assertThat(actualRecipe.getId()).isNotNull();
    }

    @Test
    void should_delete_recipe() throws IOException {
        // Given
        var recipeId = addRecipe(new RestRecipe()).getId();
        var deletePost = new HttpPost(recipeEndpoint + "/delete" + "?id=" + recipeId);
        var httpGet = new HttpGet(recipeEndpoint + "/" + recipeId);

        // When Then
        assertDoesNotThrow(() -> httpclient.execute(deletePost));
        var response = httpclient.execute(httpGet);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(404);
    }

    @Test
    void should_retrieve_recipe() throws IOException {
        // Given
        var recipeId = addRecipe(new RestRecipe()).getId();

        // When
        var retrievedRecipe = retrieveRecipe(recipeId);

        // Then
        var expectedRecipe = new RestRecipe();
        expectedRecipe.setId(recipeId);

        assertThatRecipesAreEqualIgnoringIngredientsId(retrievedRecipe, expectedRecipe);
        RecipeApiUtil.deleteAllRecipes();
    }

    @Test
    void should_update_new_recipe() throws IOException {
        // Given
        clearDataBase();
        pushItemToDb();
        populateIngredientWithItemIds();

        var updatePost = new HttpPost(recipeEndpoint);
        updatePost.setEntity(new StringEntity(tarteAuxPrunesFile, ContentType.APPLICATION_JSON));

        // When
        var response = httpclient.execute(updatePost);

        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        var recipeId = getObjectFromEntiy(response.getEntity(), RestRecipe.class).getId();

        var actualRecipes = getAllRecipe();
        var tarteAuxPrunesRecipeSummaryAfterUpdate = actualRecipes.stream().filter(restRecipeSummary ->
                restRecipeSummary.getName().equals("tarte aux prunes")).toList();
        assertThat(tarteAuxPrunesRecipeSummaryAfterUpdate).hasSize(1);

        var actualRecipe = retrieveRecipe(tarteAuxPrunesRecipeSummaryAfterUpdate.get(0).getId());

        var newTarteAuxPruneFile = populateRecipeWithId(tarteAuxPrunesFile, recipeId);
        var expectedRecipe = mapper.readValue(newTarteAuxPruneFile, RestRecipe.class);
        assertThatRecipesAreEqualIgnoringIngredientsId(actualRecipe, expectedRecipe);
    }

    @Test
    void should_update_recipe_entirely() throws IOException {
        // Given
        initDataBase();

        var recipeOriginalName = "tarte aux prunes";
        var tarteAuxPrunesRecipe = retrieveRecipe(getAllRecipe().stream().filter(restRecipeSummary ->
                restRecipeSummary.getName().equals(recipeOriginalName)).findAny().get().getId());
        var originalFirstIngredient = tarteAuxPrunesRecipe.getIngredients().get(0);
        var originalSecondIngredient = tarteAuxPrunesRecipe.getIngredients().get(1);

        float newQuantity = 0F;
        String recipeNewName = "new name";
        var newTarteAuxPrunesRecipe = getRecipeWithNewNameAndIngredient(
                tarteAuxPrunesRecipe,
                recipeNewName,
                getIngredientWithNewQuantity(originalFirstIngredient, newQuantity),
                getIngredientWithNewQuantity(originalSecondIngredient, newQuantity));

        var updatePost = new HttpPost(recipeEndpoint + "?id=" + tarteAuxPrunesRecipe.getId());
        updatePost.setEntity(new StringEntity(mapper.writeValueAsString(newTarteAuxPrunesRecipe),
                ContentType.APPLICATION_JSON));

        // When
        var response = httpclient.execute(updatePost);

        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

        var actualRecipes = getAllRecipe();

        var tarteAuxPrunesRecipeSummaryAfterUpdate = actualRecipes.stream().filter(restRecipeSummary ->
                restRecipeSummary.getName().equals(recipeNewName)).toList();
        assertThat(tarteAuxPrunesRecipeSummaryAfterUpdate).hasSize(1);

        assertThat(actualRecipes.stream().filter(restRecipeSummary ->
                restRecipeSummary.getName().equals(recipeOriginalName)).toList()).isEmpty();

        var actualRecipe = retrieveRecipe(tarteAuxPrunesRecipeSummaryAfterUpdate.get(0).getId());
        var actualFirstIngredient = actualRecipe.getIngredients().get(0);
        var actualSecondIngredient = actualRecipe.getIngredients().get(1);
        assertThat(actualFirstIngredient.getId()).isEqualTo(originalFirstIngredient.getId());
        assertThat(actualSecondIngredient.getId()).isEqualTo(originalSecondIngredient.getId());
        assertThat(actualFirstIngredient.getQuantity()).isEqualTo(newQuantity);
        assertThat(actualSecondIngredient.getQuantity()).isEqualTo(newQuantity);
    }

    @Test
    void should_retrieve_recipe_summary() throws IOException {
        // Given
        initDataBase();
        var httpGet = new HttpGet(recipeEndpoint);

        // When
        var getResponse = httpclient.execute(httpGet).getEntity();

        // Then
        var actualResponseRecipe = mapper.readValue(
                EntityUtils.toString(getResponse, "UTF-8"),
                new TypeReference<List<RestRecipeSummary>>() {
                });
        var expectedRecipe = mapper.readValue(getFileContent(RECIPE_SUMMARY_FILENAME),
                new TypeReference<List<RestRecipeSummary>>() {
                });
        putIdsToExpectedRecipes(expectedRecipe);
        assertThat(actualResponseRecipe).isEqualTo(expectedRecipe);
    }

    private void putIdsToExpectedRecipes(List<RestRecipeSummary> expectedRecipe) throws IOException {
        var recipes = getAllRecipe();
        var ids = new HashMap<String, Long>();
        for (RestRecipeSummary recipe : recipes) {
            ids.put(recipe.getName(), recipe.getId());
        }
        expectedRecipe.forEach(it -> it.setId(ids.get(it.getName())));
    }

    private void assertThatRecipesAreEqualIgnoringIngredientsId(RestRecipe actualResponseRecipe,
            RestRecipe expectedRecipe) {
        assertThat(actualResponseRecipe.getType()).isEqualTo(expectedRecipe.getType());
        assertThat(actualResponseRecipe.getSource()).isEqualTo(expectedRecipe.getSource());
        assertThat(actualResponseRecipe.getId()).isEqualTo(expectedRecipe.getId());
        assertThat(actualResponseRecipe.getSeason()).isEqualTo(expectedRecipe.getSeason());
        assertThat(actualResponseRecipe.getName()).isEqualTo(expectedRecipe.getName());
        List<RestIngredient> actualIngredients = actualResponseRecipe.getIngredients();
        List<RestIngredient> expectedIngredients = expectedRecipe.getIngredients();

        if (null == actualIngredients) {
            assertThat(expectedIngredients).isNull();
        } else {
            assertThat(actualIngredients).hasSameSizeAs(expectedIngredients);
            for (int index = 0; index < actualIngredients.size(); index++) {
                assertThat(actualIngredients.get(index).getItemId())
                        .isEqualTo(expectedIngredients.get(index).getItemId());
                assertThat(actualIngredients.get(index).getQuantity())
                        .isEqualTo(expectedIngredients.get(index).getQuantity());
            }
        }
    }
}
