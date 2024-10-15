package com.arwall.nosrecettes.domain.gatesToInfra.spi;

import java.util.List;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.Item;

public interface PersistItem {

    Optional<Item> findById(Long id);

    List<Item> findAll();

    Item update(Item item);
    Item create();

    void delete(long id);
}
