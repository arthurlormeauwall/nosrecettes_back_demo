package com.arwall.nosrecettes.demoutil.importcsv;


import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.domain.model.QuantityType;
import com.arwall.nosrecettes.rest.controller.ItemController;
import com.arwall.nosrecettes.rest.model.RestItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class ImportItemUtil {

    ItemController itemController;

    @Autowired
    ImportItemUtil(ItemController itemController) {
        this.itemController = itemController;
    }

    public static QuantityType resolveQuantityType(String type) {
        if (type.equals("QTE"))
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

    public void importItem(String previewsItemToImport, String itemToImport, String nextItemToImport, String type) {
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

    private boolean shouldAddTypeToName(String previewsItemToImport, String nextItemToImport, String itemName) {
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

    private void pushItem(Item item) {
        itemController.update(new RestItem((item)));
    }
}
