package com.arwall.nosrecettes.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistItem;
import com.arwall.nosrecettes.persistence.model.ItemData;
import com.arwall.nosrecettes.persistence.repo.ItemRepository;

@Service
public class ItemPersist implements PersistItem {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id).map(ItemData::toDomain);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll().stream().map(ItemData::toDomain).toList();
    }

    @Override
    public Item update(Item item) {
        return itemRepository.save(new ItemData(item)).toDomain();
    }

    @Override
    public Item create() {
        return itemRepository.save(new ItemData()).toDomain();
    }

    @Override
    public void delete(long id) {
        itemRepository.deleteById(id);
    }
}
