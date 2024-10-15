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

import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.rest.model.RestItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemApiUtil {

    public final static String itemEndpoint = url() + "/item";

    public static RestItem createNewItem() throws IOException {
        var httpclient = HttpClients.createDefault();
        HttpPost createPost = new HttpPost(itemEndpoint);
        createPost.setEntity(new StringEntity(new ObjectMapper()
                .writeValueAsString(new RestItem()),
                ContentType.APPLICATION_JSON));
        var entity = httpclient.execute(createPost).getEntity();
        return getObjectFromEntiy(entity, RestItem.class);
    }

    public static void updateItem(long itemId, String file) throws IOException {
        var updatePost = new HttpPost(itemEndpoint);
        updatePost.setHeader("Content-type", "application/json");
        var newFile = populateItemWithID(itemId, file);
        updatePost.setEntity(new StringEntity(newFile, ContentType.APPLICATION_JSON));
        HttpClients.createDefault().execute(updatePost);
    }

    public static String populateItemWithID(Long itemId, String file) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var object = mapper.readValue(file, RestItem.class);
        object = new RestItem(Item.builder(object.toDomain()).withId(itemId).build());
        return mapper.writeValueAsString(object);
    }


    public static void pushItem(String file) throws IOException {
        var id = createNewItem().getId();
        updateItem(id, file);
    }

    public static void pushItems(List<String> files) {
        files.stream().forEach(file -> {
            try {
                pushItem(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void deleteItem(Long id) throws IOException {
        var httpclient = HttpClients.createDefault();
        var updatePost = new HttpPost(itemEndpoint + "/delete" + "?id=" + id);
        updatePost.setHeader("Content-type", "application/json");
        httpclient.execute(updatePost);
    }

    public static RestItem retrieveItem(Long id) throws IOException {
        var httpClient = HttpClients.createDefault();
        var httpGet = new HttpGet(itemEndpoint + "/" + id);
        var getResponse = httpClient.execute(httpGet).getEntity();
        return getObjectFromEntiy(getResponse, RestItem.class);
    }

    public static List<RestItem> getAllItems() throws IOException {
        var httpclient = HttpClients.createDefault();
        var updatePost = new HttpGet(itemEndpoint);
        var response = httpclient.execute(updatePost);
        return new ObjectMapper().readValue(
                EntityUtils.toString(response.getEntity(), "UTF-8"),
                new TypeReference<>() {
                });
    }

    public static void deleteAllItems() throws IOException {
        getAllItems().stream().forEach(item -> {
            try {
                deleteItem(item.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
