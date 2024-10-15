package com.arwall.nosrecettes.domain.stubfactories;

import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.domain.model.QuantityType;

public class ItemStubs {
    public static final Long AN_ITEM_ID = 1L;
    public static final Long A_NEW_ITEM_ID = 2L;
    public static final String AN_ITEM_NAME = "a_name";
    public static final QuantityType A_QUANTITY_TYPE = QuantityType.GR;
    public static final Item AN_ITEM = Item.builder()
            .withId(AN_ITEM_ID)
            .withName(AN_ITEM_NAME)
            .withQuantityType(A_QUANTITY_TYPE)
            .build();

    public static final Item A_NEW_ITEM = Item.builder()
            .withId(A_NEW_ITEM_ID)
            .build();


}
