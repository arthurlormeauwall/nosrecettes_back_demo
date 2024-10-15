package com.arwall.nosrecettes.infra;


import static com.arwall.nosrecettes.infra.apiutil.CommonUtil.getObjectFromEntiy;
import static com.arwall.nosrecettes.infra.apiutil.ItemApiUtil.createNewItem;
import static com.arwall.nosrecettes.infra.apiutil.ItemApiUtil.deleteAllItems;
import static com.arwall.nosrecettes.infra.apiutil.ItemApiUtil.itemEndpoint;
import static com.arwall.nosrecettes.infra.apiutil.ItemApiUtil.populateItemWithID;
import static com.arwall.nosrecettes.infra.apiutil.ItemApiUtil.updateItem;
import static com.arwall.nosrecettes.infra.util.FileUtil.FRAISE_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.PRUNE_FILENAME;
import static com.arwall.nosrecettes.infra.util.FileUtil.getFileContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.arwall.nosrecettes.Application;
import com.arwall.nosrecettes.rest.model.RestItem;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = Application.class)
@SpringBootConfiguration
@ActiveProfiles(value = "dev")
public class ItemIT {

    private HttpClient httpclient;
    private ObjectMapper mapper;
    private String fraiseFile;
    private String prunesFile;

    @BeforeEach
    void setup() throws IOException {
        mapper = new ObjectMapper();
        httpclient = HttpClients.createDefault();
        fraiseFile = getFileContent(FRAISE_FILENAME);
        prunesFile = getFileContent(PRUNE_FILENAME);
    }

    @AfterEach
    void after() throws IOException {
        deleteAllItems();
    }

    @Test
    void should_create_new_item() throws IOException {
        // Given
        HttpPost createPost = new HttpPost(itemEndpoint);
        createPost.setEntity(new StringEntity(new ObjectMapper()
                .writeValueAsString(new RestItem()),
                ContentType.APPLICATION_JSON));

        // When
        HttpResponse createResponse = httpclient.execute(createPost);

        // Then
        assertThat(createResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        HttpEntity entity = createResponse.getEntity();
        var itemId = getObjectFromEntiy(entity, RestItem.class).getId();
        assertThat(itemId).isNotNull();
    }

    @Test
    void should_delete_item() throws IOException {
        // Given
        var id = createNewItem().getId();
        var updatePost = new HttpPost(itemEndpoint + "/delete" + "?id=" + id);
        updatePost.setHeader("Content-type", "application/json");
        var httpGet = new HttpGet(itemEndpoint + "/" + id);

        // When Then
        assertDoesNotThrow(() -> httpclient.execute(updatePost));
        var response = httpclient.execute(httpGet);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(404);
    }

    @Test
    void should_update_item() throws IOException {
        // Given
        var itemId = createNewItem().getId();
        var updatePost = new HttpPost(itemEndpoint);
        updatePost.setHeader("Content-type", "application/json");
        var newfraiseFile = populateItemWithID(itemId, fraiseFile);
        updatePost.setEntity(new StringEntity(newfraiseFile, ContentType.APPLICATION_JSON));

        // When
        var response = httpclient.execute(updatePost);

        // Then
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
    }


    @Test
    void should_retrieve_item() throws IOException {
        // Given
        var itemId = createNewItem().getId();
        updateItem(itemId, fraiseFile);
        var httpGet = new HttpGet(itemEndpoint + "/" + itemId);

        // When
        var getResponse = httpclient.execute(httpGet).getEntity();

        // Then
        var actualResponseRecipe = mapper.readValue(
                EntityUtils.toString(getResponse, "UTF-8"),
                RestItem.class);

        var expectedRecipe = mapper.readValue(fraiseFile, RestItem.class);
        expectedRecipe.setId(itemId);

        assertThat(actualResponseRecipe).isEqualTo(expectedRecipe);
    }
}
