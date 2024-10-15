package com.arwall.nosrecettes.infra;

import static com.arwall.nosrecettes.infra.apiutil.CommonUtil.getObjectFromEntiy;
import static com.arwall.nosrecettes.infra.apiutil.MenuApiUtil.addTwoRecipesToMenus;
import static com.arwall.nosrecettes.infra.apiutil.MenuApiUtil.removeRecipeFromMenu;
import static com.arwall.nosrecettes.infra.apiutil.MenuApiUtil.updateIngredientOfMenu;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.getAllRecipe;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.retrieveRecipe;
import static com.arwall.nosrecettes.infra.apiutil.ShoppingListApiUtil.deleteAllIngredientFromShoppingList;
import static com.arwall.nosrecettes.infra.apiutil.ShoppingListApiUtil.getRestIngredientFromMenus;
import static com.arwall.nosrecettes.infra.apiutil.ShoppingListApiUtil.pushShoppingListIngredient;
import static com.arwall.nosrecettes.infra.apiutil.ShoppingListApiUtil.retrieveAllIngredientFromShoppingList;
import static com.arwall.nosrecettes.infra.apiutil.ShoppingListApiUtil.shoppingListEndpoint;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.arwall.nosrecettes.domain.model.IngredientFromMenu;
import com.arwall.nosrecettes.domain.model.IngredientFromShoppingList;
import com.arwall.nosrecettes.rest.model.RestIngredient;
import com.arwall.nosrecettes.rest.model.RestIngredientFromMenu;
import com.arwall.nosrecettes.rest.model.RestIngredientFromShoppingList;
import com.arwall.nosrecettes.rest.model.RestRecipe;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(value = "dev")
public class ShoppingListIT extends AbstractIntegrationT {


    ShoppingListIT() throws IOException {
    }

    @BeforeEach
    void setup() throws IOException {
        initDataBase();
    }

    @Test
    void should_add_to_shoppingListFromMenu_when_add_new_recipe_to_menu() throws IOException {
        // Given
        var recipes = getAllRecipe();
        var firstId = recipes.get(0).getId(); // tarte aux fraises
        var secondId = recipes.get(2).getId(); // pizza

        // When
        var response = addTwoRecipesToMenus(firstId, secondId);

        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        var actualIngredientFromMenu = getRestIngredientFromMenus();
        var tarteAuxFraisesRecipe = mapper.readValue(tarteAuxFraisesFile, RestRecipe.class);
        var pizzaRecipe = mapper.readValue(pizzaFile, RestRecipe.class);

        assertThat(actualIngredientFromMenu).hasSize(3);
        RestIngredient targeAuxFraiseFirstIngredient = tarteAuxFraisesRecipe.getIngredients().getFirst();
        RestIngredient pizzaFirstIngredient = pizzaRecipe.getIngredients().getFirst();

        RestIngredient tarteAuxFraisesSecondIngredient = tarteAuxFraisesRecipe.getIngredients().get(1);
        RestIngredient pizzaSecondIngredient = pizzaRecipe.getIngredients().get(1);

        assertThatIngredientContainsIgnoringIngredientIds(actualIngredientFromMenu,
                List.of(
                        IngredientFromMenu.builder()
                                .withItemId(targeAuxFraiseFirstIngredient.getItemId())
                                .withQuanties(Map.of(
                                        tarteAuxFraisesRecipe.getId(),
                                        targeAuxFraiseFirstIngredient.getQuantity()
                                ))
                                .withRecipeId(Set.of(tarteAuxFraisesRecipe.getId()))
                                .build(),
                        IngredientFromMenu.builder()
                                .withItemId(pizzaFirstIngredient.getItemId())
                                .withQuanties(Map.of(
                                        pizzaRecipe.getId(),
                                        pizzaFirstIngredient.getQuantity()
                                ))
                                .withRecipeId(Set.of(pizzaRecipe.getId()))
                                .build(),
                        IngredientFromMenu.builder()
                                .withItemId(tarteAuxFraisesSecondIngredient.getItemId())
                                .withQuanties(Map.of(
                                        tarteAuxFraisesRecipe.getId(),
                                        tarteAuxFraisesSecondIngredient.getQuantity(),
                                        pizzaRecipe.getId(),
                                        pizzaSecondIngredient.getQuantity()
                                ))
                                .withRecipeId(Set.of(tarteAuxFraisesRecipe.getId(), pizzaRecipe.getId()))
                                .build()
                ));
    }

    private void assertThatIngredientContainsIgnoringIngredientIds(
            List<RestIngredientFromMenu> actualIngredientFromMenu,
            List<IngredientFromMenu> ingredientThatShouldBeInFirstList) {
        for (RestIngredientFromMenu actualIngredient : actualIngredientFromMenu) {
            assertThat(ingredientThatShouldBeInFirstList.stream()
                    .anyMatch(
                            ingredient -> areIngredientAreTheSameWithQuantityIgnoringId(ingredient, actualIngredient)))
                    .isTrue();
        }
    }

    @Test
    void should_remove_to_shoppingListFromMenu_when_remove_recipe_menu() throws IOException {
        // Given
        var recipes = getAllRecipe();
        var tarteAuxFraisesId = recipes.get(0).getId(); // tarte aux fraises
        var pizzaId = recipes.get(2).getId(); // pizza
        addTwoRecipesToMenus(tarteAuxFraisesId, pizzaId);

        // When
        var response = removeRecipeFromMenu(tarteAuxFraisesId);

        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        var actualIngredientFromMenu = getRestIngredientFromMenus();

        var pizzaRecipe = mapper.readValue(pizzaFile, RestRecipe.class);

        assertThat(actualIngredientFromMenu).hasSize(2);

        assertThatIngredientContainsIgnoringIngredientIds(actualIngredientFromMenu,
                List.of(
                        IngredientFromMenu.builder()
                                .withItemId(pizzaRecipe.getIngredients().getFirst().getItemId())
                                .withQuanties(Map.of(pizzaRecipe.getId(),
                                        pizzaRecipe.getIngredients().getFirst().getQuantity()))
                                .withRecipeId(Set.of(pizzaRecipe.getId()))
                                .build(),
                        IngredientFromMenu.builder()
                                .withItemId(pizzaRecipe.getIngredients().get(1).getItemId())
                                .withQuanties(
                                        Map.of(pizzaRecipe.getId(), pizzaRecipe.getIngredients().get(1).getQuantity()))
                                .withRecipeId(Set.of(pizzaRecipe.getId()))
                                .build()));
    }


    @Test
    void should_update_ingredient_from_shoppingListFromMenu() throws IOException {
        // Given
        var recipes = getAllRecipe();

        var tarteAuxFraisesId = recipes.get(0).getId(); // tarte aux fraises
        var pizzaId = recipes.get(2).getId(); // pizza
        addTwoRecipesToMenus(tarteAuxFraisesId, pizzaId);

        var firstIngredientOfPizza = getFirstIngredientOfRecipe(pizzaId);
        var newQuantityOfFirstIngredientOfPizza = 10000F;
        firstIngredientOfPizza.setQuantity(newQuantityOfFirstIngredientOfPizza);

        // When
        var response = updateIngredientOfMenu(firstIngredientOfPizza);

        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        var actualIngredientFromMenuAfterUpdate = getRestIngredientFromMenus();

        var actualIngredientsOfPizzaAfterUpdate = actualIngredientFromMenuAfterUpdate.stream()
                .filter(restIngredientFromMenu -> restIngredientFromMenu.getRecipeId().contains(pizzaId))
                .toList();
        assertThat(actualIngredientsOfPizzaAfterUpdate).hasSize(2);

        var tarteAuxFraiseRecipe = retrieveRecipe(tarteAuxFraisesId);

        var actualFirstIngredientOfPizzaAfterUpdate = actualIngredientsOfPizzaAfterUpdate.getFirst();
        assertIngredientAreTheSameWithQuantity(actualFirstIngredientOfPizzaAfterUpdate, firstIngredientOfPizza,
                newQuantityOfFirstIngredientOfPizza);
    }

    private void assertIngredientAreTheSameWithQuantity(RestIngredientFromMenu actualIngredientFromMenu,
            RestIngredientFromMenu expectedIngredientFromMenu,
            float newQuantity) {
        assertThat(actualIngredientFromMenu.getId()).isEqualTo(expectedIngredientFromMenu.getId());
        assertThat(actualIngredientFromMenu.getQuantity()).isEqualTo(newQuantity);
        assertThat(actualIngredientFromMenu.getItemId()).isEqualTo(expectedIngredientFromMenu.getItemId());
        assertThat(actualIngredientFromMenu.getRecipeId()).isEqualTo(
                expectedIngredientFromMenu.getRecipeId());
    }

    @Test
    void should_force_sync() throws IOException {
//        var recipes = getAllRecipe();
//        var tarteAuxFraisesId = recipes.get(0).getId(); // tarte aux fraises
//        var pizzaId = recipes.get(2).getId(); // pizza
//
//        addTwoRecipesToMenus(tarteAuxFraisesId, pizzaId);
//        var firstIngredientOfPizza = getFirstIngredientOfRecipe(pizzaId);
//        var firstIngredientOfTarte = getFirstIngredientOfRecipe(tarteAuxFraisesId);
//        float newQuantity = 10000F;
//        updateIngredientOfMenu(getRestIngredientFromMenuWithNewQuantity(firstIngredientOfPizza, newQuantity));
//        updateIngredientOfMenu(getRestIngredientFromMenuWithNewQuantity(firstIngredientOfTarte, newQuantity));
//        var postForceSync = new HttpPost(shoppingListEndpoint + "/frommenu/forcesync" + "?id=" + pizzaId);
//
//        // When
//        var response = httpclient.execute(postForceSync);
//
//        // Then
//        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
//        var actualIngredientFromMenuAfterForceSync = getRestIngredientFromMenus();
//        var actualFistIngredientsOfPizza = actualIngredientFromMenuAfterForceSync.stream()
//                .filter(restIngredientFromMenu -> restIngredientFromMenu.getRecipeId().contains(pizzaId))
//                .toList().getFirst();
//        var actualFistIngredientsOfTarteAuxFraises = actualIngredientFromMenuAfterForceSync.stream()
//                .filter(restIngredientFromMenu -> restIngredientFromMenu.getRecipeId().contains(tarteAuxFraisesId))
//                .toList().getFirst();
//        assertIngredientAreTheSameWithQuantityIgnoringId(actualFistIngredientsOfPizza,
//                firstIngredientOfPizza,
//                firstIngredientOfPizza.getQuantity());
//        assertIngredientAreTheSameWithQuantityIgnoringId(actualFistIngredientsOfTarteAuxFraises,
//                firstIngredientOfTarte,
//                newQuantity);
    }

    private void assertIngredientAreTheSameWithQuantityIgnoringId(
            RestIngredientFromMenu actualIngredient,
            RestIngredientFromMenu expectedIngredient,
            float expectedQuantity) {
        assertThat(actualIngredient.getQuantities()).isEqualTo(expectedQuantity);
        assertThat(actualIngredient.getItemId()).isEqualTo(expectedIngredient.getItemId());
        assertThat(actualIngredient.getRecipeId()).isEqualTo(
                expectedIngredient.getRecipeId());
    }

    private void assertIngredientAreTheSameWithQuantityIgnoringId(RestIngredientFromShoppingList actualIngredient,
            RestIngredientFromShoppingList expectedIngredient) {
        assertThat(actualIngredient.getQuantity()).isEqualTo(expectedIngredient.getQuantity());
        assertThat(actualIngredient.getItemId()).isEqualTo(expectedIngredient.getItemId());
    }

    public static boolean areIngredientAreTheSameWithQuantityIgnoringId(IngredientFromMenu actualIngredient,
            RestIngredientFromMenu expectedIngredient) {
        return actualIngredient.getQuantitiesPerRecipes()
                .equals(expectedIngredient.toDomain().getQuantitiesPerRecipes())
                && actualIngredient.getItemId().equals(expectedIngredient.getItemId());
    }

    private RestIngredientFromMenu getFirstIngredientOfRecipe(Long pizzaId) throws IOException {
        return getRestIngredientFromMenus().stream()
                .filter(restIngredientFromMenu -> restIngredientFromMenu.getRecipeId().contains(pizzaId)).findFirst()
                .get();
    }



    private void verifyIngredient(RestIngredientFromMenu actualIngredient,
            RestIngredient expectedIngredient, Long id) {
        assertThat(actualIngredient.getItemId()).isEqualTo(expectedIngredient.getItemId());
        assertThat(actualIngredient.getQuantities()).isEqualTo(expectedIngredient.getQuantity());
        assertThat(actualIngredient.getRecipeId()).isEqualTo(id);
    }

    @Test
    void should_create_new_ingredient_in_shoppingList() throws IOException {
        // Given
        HttpPost createPost = new HttpPost(shoppingListEndpoint);
        createPost.setHeader("Content-type", "application/json");
        createPost.setEntity(new StringEntity(new ObjectMapper()
                .writeValueAsString(new RestIngredientFromShoppingList()),
                ContentType.APPLICATION_JSON));

        // When
        HttpResponse createResponse = httpclient.execute(createPost);

        // Then
        assertThat(createResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        HttpEntity entity = createResponse.getEntity();
        var itemId = getObjectFromEntiy(entity, RestIngredientFromShoppingList.class).getId();
        assertThat(itemId).isNotNull();
    }

    @Test
    void should_delete_ingredient_from_shopping_list() throws IOException {
        // Given
        var recipeId = pushShoppingListIngredient().getId();
        var deletePost = new HttpPost(shoppingListEndpoint + "/delete" + "?id=" + recipeId);
        var httpGet = new HttpGet(shoppingListEndpoint + "/" + recipeId);

        // When Then
        assertDoesNotThrow(() -> httpclient.execute(deletePost));
        var response = httpclient.execute(httpGet);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(404);
    }

    @Test
    void should_retrieve_all_ingredients() throws IOException {
        // Given
        deleteAllIngredientFromShoppingList();
        var recipeId = pushShoppingListIngredient().getId();

        // When
        var retrievedIngredients = retrieveAllIngredientFromShoppingList();

        // Then
        var expectedIngredient = new RestIngredientFromShoppingList();
        expectedIngredient.setId(recipeId);

        assertThat(retrievedIngredients).hasSize(1);
        assertIngredientAreTheSameWithQuantityIgnoringId(retrievedIngredients.getFirst(), expectedIngredient);
    }

    @Test
    void should_update_new_ingredient_in_shoppingList() throws IOException {
        // Given
        deleteAllIngredientFromShoppingList();
        var id = pushShoppingListIngredient().getId();

        HttpPost updatePost = new HttpPost(shoppingListEndpoint);
        updatePost.setHeader("Content-type", "application/json");
        RestIngredientFromShoppingList expected = new RestIngredientFromShoppingList(
                IngredientFromShoppingList.builder()
                        .withId(id)
                        .withQuantiy(10F)
                        .withItemId(3L)
                        .build());
        updatePost.setEntity(new StringEntity(new ObjectMapper()
                .writeValueAsString(expected),
                ContentType.APPLICATION_JSON));

        // When
        HttpResponse createResponse = httpclient.execute(updatePost);

        // Then
        assertThat(createResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        HttpEntity entity = createResponse.getEntity();
        var actual = getObjectFromEntiy(entity, RestIngredientFromShoppingList.class);
        assertThat(actual).isEqualTo(expected);
    }
}
