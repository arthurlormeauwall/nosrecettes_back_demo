package com.arwall.nosrecettes.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arwall.nosrecettes.domain.model.IngredientFromShoppingList;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistIngredientFromShoppingList;
import com.arwall.nosrecettes.persistence.model.IngredientFromShoppingListData;
import com.arwall.nosrecettes.persistence.repo.IngredientFromShoppingListRepository;

@Service
public class IngredientFromShoppingListPersist implements PersistIngredientFromShoppingList {

    @Autowired
    private IngredientFromShoppingListRepository repository;

    @Override
    public Optional<IngredientFromShoppingList> findById(Long id) {
        return repository.findById(id).map(IngredientFromShoppingListData::toDomain);
    }

    @Override
    public List<IngredientFromShoppingList> findAll() {
        return repository.findAll().stream()
                .map(IngredientFromShoppingListData::toDomain).toList();
    }

    @Override
    public IngredientFromShoppingList update(IngredientFromShoppingList ingredient) {
        return repository.save(new IngredientFromShoppingListData(ingredient)).toDomain();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
