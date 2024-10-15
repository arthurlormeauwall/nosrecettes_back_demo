package com.arwall.nosrecettes.domain.model;

import lombok.Getter;

import com.arwall.nosrecettes.domain.ddd.DomainEntity;

@Getter
public class Item extends DomainEntity {

    private final String name;
    private final QuantityType quantityType;
    private final String type;

    private Item(Builder builder) {
        super(builder.id);
        this.name = builder.name;
        this.quantityType = builder.quantityType;
        this.type=builder.type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Item recipe) {
        return new Builder(recipe);
    }

    public static class Builder {

        private Long id;
        private String name;
        private String type;
        private QuantityType quantityType;


        public Builder() {
        }

        public Builder(Item item) {
            this.id=item.getId();
            this.name= item.getName();
            this.quantityType=item.getQuantityType();
            this.type=item.getType();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withQuantityType(QuantityType quantityType) {
            this.quantityType = quantityType;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }
}
