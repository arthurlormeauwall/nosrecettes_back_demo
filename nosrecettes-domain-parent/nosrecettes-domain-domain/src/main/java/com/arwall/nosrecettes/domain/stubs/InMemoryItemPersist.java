package com.arwall.nosrecettes.domain.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistItem;

public class InMemoryItemPersist implements PersistItem {

    Map<Long, Item> repository;

    public InMemoryItemPersist() {
        repository = new HashMap<>();
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<Item> findAll() {
        var response = new ArrayList();
        var it = repository.entrySet().iterator();
        while (it.hasNext()) {
            response.add(it.next().getValue());
        }
        return response;
    }

    @Override
    public Item update(Item ingredient) {
        repository.put(ingredient.getId(), ingredient);
        return ingredient;
    }

    @Override
    public Item create() {
        var it = repository.entrySet().iterator();
        Long nextIndex = null;
        if(it.hasNext() == false){
            nextIndex=0L;
        }else{
            while (it.hasNext()) {
                nextIndex = it.next().getValue().getId() + 1;
            }
        }
        Item item = Item.builder().withId(nextIndex).build();
        repository.put(nextIndex, item);
        return item;
    }

    @Override
    public void delete(long id) {
        repository.remove(id);
    }
}
