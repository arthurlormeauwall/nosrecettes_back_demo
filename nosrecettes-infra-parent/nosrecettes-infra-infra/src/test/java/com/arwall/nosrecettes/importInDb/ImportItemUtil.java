package com.arwall.nosrecettes.importInDb;

import static com.arwall.nosrecettes.infra.apiutil.ItemApiUtil.itemEndpoint;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.domain.model.QuantityType;
import com.arwall.nosrecettes.rest.model.RestItem;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImportItemUtil {

    public static void importItem(String previewsItemToImport, String itemToImport, String nextItemToImport, String type)
            throws IOException {
        var itemAndType = Arrays.stream(itemToImport.split(" _")).toList();
        var itemName = itemAndType.getFirst();
        var quantityType = itemAndType.get(1);
        if (shouldAddTypeToName(previewsItemToImport, nextItemToImport, itemName)) {
            itemName = itemName + " " + quantityType;
        }
        Item item = Item.builder()
                .withName(itemName)
                .withQuantityType(resolveQuantityType(quantityType))
                .withType(type)
                .build();
        pushItem(item);
    }

    private static boolean shouldAddTypeToName(String previewsItemToImport, String nextItemToImport, String itemName) {
        String nextItemName = null;
        String previewsItemName = null;
        if (null != nextItemToImport) {
            var nextItemAndType = Arrays.stream(nextItemToImport.split(" _")).toList();
            nextItemName = nextItemAndType.getFirst();
        }
        if (null != previewsItemToImport) {
            var previewsItemType = Arrays.stream(previewsItemToImport.split(" _")).toList();
            previewsItemName = previewsItemType.getFirst();
        }
        return itemName.equals(nextItemName) || itemName.equals(previewsItemName);
    }

    private static void pushItem(Item item) throws IOException {
        var updatePost = new HttpPost(itemEndpoint);
        updatePost.setHeader("Content-type", "application/json");

        var restItem = new RestItem(item);
        updatePost.setEntity(
                new StringEntity(new ObjectMapper().writeValueAsString(restItem), ContentType.APPLICATION_JSON));

        HttpClients.createDefault().execute(updatePost);
    }

    public static QuantityType resolveQuantityType(String type) {
            if (type.equals(new String("QTE")))
                return QuantityType.AMOUNT;
        if (type.equals("GR"))
                return QuantityType.GR;
        if (type.equals("ML"))
                return QuantityType.ML;

        return null;
    }

    public static int previewsIndex(int index) {
        return index - 1;
    }

    public static int nextIndex(int index) {
        return index + 1;
    }

}
