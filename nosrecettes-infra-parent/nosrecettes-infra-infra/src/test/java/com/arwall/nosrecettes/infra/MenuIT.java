package com.arwall.nosrecettes.infra;

import static com.arwall.nosrecettes.infra.apiutil.MenuApiUtil.addTwoRecipesToMenus;
import static com.arwall.nosrecettes.infra.apiutil.MenuApiUtil.getRestRecipeSummaries;
import static com.arwall.nosrecettes.infra.apiutil.MenuApiUtil.menuEndpoint;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.deleteRecipe;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.getAllRecipe;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.retrieveRecipe;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.arwall.nosrecettes.rest.model.RestRecipe;
import com.arwall.nosrecettes.rest.model.RestRecipeSummary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@ActiveProfiles(value = "dev")
public class MenuIT extends AbstractIntegrationT {

    MenuIT() throws IOException {
        super();
    }

    @BeforeEach
    void setup() throws IOException {
        initDataBase();
    }



    @Test
    void should_get_empty_menu_at_beginning() throws IOException {
        // Given
        var get = new HttpGet(menuEndpoint);

        // When
        var response = EntityUtils.toString(httpclient.execute(get).getEntity());

        // Then
        var responeObject = mapper.readValue(response, new TypeReference<List<RestRecipeSummary>>() {
        });
        assertThat(responeObject).isEmpty();
    }

    @Test
    void should_create_a_menu() throws IOException {
        // Given
        var recipes = getAllRecipe();
        var firstId = recipes.get(0).getId();
        var secondId = recipes.get(2).getId();

        // sanity check
        var tarteAuxFraiseRecipe = new ObjectMapper().readValue(tarteAuxFraisesFile, RestRecipe.class);
        var pizzaRecipe = new ObjectMapper().readValue(pizzaFile, RestRecipe.class);
        assertThat(recipes.get(0).getName()).isEqualTo(tarteAuxFraiseRecipe.getName());
        assertThat(recipes.get(2).getName()).isEqualTo(pizzaRecipe.getName());

        // When
        addTwoRecipesToMenus(firstId, secondId);

        // Then
        var menuRecipes = getRestRecipeSummaries();

        assertThat(menuRecipes).hasSize(2);

        var firstRecipeOfMenu = menuRecipes.get(0);
        var secondRecipeOfMenu = menuRecipes.get(1);
        assertThat(firstRecipeOfMenu.getId()).isEqualTo(firstId);
        assertThat(secondRecipeOfMenu.getId()).isEqualTo(secondId);
        assertThat(firstRecipeOfMenu.getName()).isEqualTo(tarteAuxFraiseRecipe.getName());
        assertThat(secondRecipeOfMenu.getName()).isEqualTo(pizzaRecipe.getName());
    }

    @Test
    void should_remove_recipe_from_menu() throws IOException {
        // Given
        var recipes = getAllRecipe();

        Long firstId = recipes.get(0).getId();
        Long secondId = recipes.get(2).getId();
        addTwoRecipesToMenus(firstId, secondId);

        var removeRecipes = new HttpPost(menuEndpoint + "/remove" + "?ids=" + firstId);
        var getMenu = new HttpGet(menuEndpoint);

        // When
        httpclient.execute(removeRecipes);
        httpclient = HttpClients.createDefault();
        var menus = httpclient.execute(getMenu);

        // Then
        var menuRecipes = mapper.readValue(EntityUtils.toString(menus.getEntity()),
                new TypeReference<List<RestRecipeSummary>>() {
                });
        var pizzaRecipe = new ObjectMapper().readValue(pizzaFile, RestRecipe.class);

        assertThat(menuRecipes).hasSize(1);
        var firstRecipeOfMenu = menuRecipes.get(0);
        assertThat(firstRecipeOfMenu.getId()).isEqualTo(secondId);
        assertThat(firstRecipeOfMenu.getName()).isEqualTo(pizzaRecipe.getName());
    }

    @Test
    void should_not_delete_recipe_when_remove_recipe_from_menu() throws IOException {
        // Given
        var recipes = getAllRecipe();

        Long firstId = recipes.get(0).getId();
        Long secondId = recipes.get(2).getId();
        addTwoRecipesToMenus(firstId, secondId);
        var removeRecipes = new HttpPost(menuEndpoint + "/remove" + "?ids=" + firstId);
        httpclient.execute(removeRecipes);

        // When Then
        assertDoesNotThrow(() -> retrieveRecipe(firstId));
    }

    @Test
    void should_remove_from_menus_if_delete_recipe() throws IOException {
        // Given
        var recipes = getAllRecipe();
        Long firstId = recipes.get(0).getId();
        Long secondId = recipes.get(2).getId();
        addTwoRecipesToMenus(firstId, secondId);

        // When
        deleteRecipe(firstId);

        // Then
        var getMenu = new HttpGet(menuEndpoint);
        httpclient = HttpClients.createDefault();
        HttpEntity entity = httpclient.execute(getMenu).getEntity();
        var menuRecipes = mapper.readValue(EntityUtils.toString(entity),
                new TypeReference<List<RestRecipeSummary>>() {
                });
        var pizzaRecipe = new ObjectMapper().readValue(pizzaFile, RestRecipe.class);

        assertThat(menuRecipes).hasSize(1);
        var firstRecipeOfMenu = menuRecipes.get(0);
        assertThat(firstRecipeOfMenu.getId()).isEqualTo(secondId);
        assertThat(firstRecipeOfMenu.getName()).isEqualTo(pizzaRecipe.getName());
    }
}
