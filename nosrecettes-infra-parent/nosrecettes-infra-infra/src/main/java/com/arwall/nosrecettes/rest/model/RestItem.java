package com.arwall.nosrecettes.rest.model;

import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.domain.model.QuantityType;

import lombok.Data;

@Data
public class RestItem {

    private String name;
    private QuantityType quantityType;
    private Long id;
    private String type;

    public RestItem() {
    }

    public RestItem(Item item) {
        this.name = item.getName();
        this.quantityType = item.getQuantityType();
        this.id = item.getId();
        this.type= item.getType();
    }

    public Item toDomain() {
        return Item.builder()
                .withName(this.name)
                .withQuantityType(this.quantityType)
                .withId(this.id)
                .withType(this.type)
                .build();
    }
}
