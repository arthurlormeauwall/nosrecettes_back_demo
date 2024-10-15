package com.arwall.nosrecettes.domain.service;

import java.util.List;


import com.arwall.nosrecettes.domain.gatesToInfra.api.ItemCrudOperations;
import com.arwall.nosrecettes.domain.exception.RecipeNotFoundException;
import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistItem;

public class ItemService implements ItemCrudOperations {

    PersistItem itemPersist;

    public ItemService(PersistItem itemPersist) {
        this.itemPersist = itemPersist;
    }

    @Override
    public Item retrieve(Long id) {
        var recipeOpt = itemPersist.findById(id);
        if (recipeOpt.isPresent()) {
            return recipeOpt.get();
        } else {
            throw new RecipeNotFoundException(id);
        }
    }

    @Override
    public void delete(long id) {
        itemPersist.delete(id);
    }

    @Override
    public Item update(Item item) {
        Long id = item.getId();
        if (null == id) {
            var newItem = itemPersist.create();
            item = Item.builder(item)
                    .withId(newItem.getId())
                    .build();
        }
        return itemPersist.update(item);
    }

    @Override
    public List<Item> findAll() {
        return itemPersist.findAll();
    }
}
