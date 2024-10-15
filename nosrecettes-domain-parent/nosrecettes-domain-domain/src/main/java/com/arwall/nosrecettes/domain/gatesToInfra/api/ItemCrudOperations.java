package com.arwall.nosrecettes.domain.gatesToInfra.api;

import java.util.List;

import com.arwall.nosrecettes.domain.model.Item;

public interface ItemCrudOperations {

    Item retrieve(Long id);

    void delete(long id);

    Item update(Item item);

    List<Item> findAll();
}
