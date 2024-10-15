package com.arwall.nosrecettes.persistence.model;

import java.util.Objects;

import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.domain.model.QuantityType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ItemData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private QuantityType quantityType;
    private String type;

    public ItemData() {
    }

    public ItemData(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.quantityType = item.getQuantityType();
        this.type=item.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemData itemData)) {
            return false;
        }
        return Objects.equals(
                getId(), itemData.getId())
                &&
                Objects.equals(getName(), itemData.getName())
                &&
                Objects.equals(getType(), itemData.getType())
                &&
                getQuantityType() == itemData.getQuantityType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getName(),
                getType(),
                getQuantityType());
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
