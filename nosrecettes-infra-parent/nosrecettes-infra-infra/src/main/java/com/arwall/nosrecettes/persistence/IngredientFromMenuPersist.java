package com.arwall.nosrecettes.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arwall.nosrecettes.domain.model.IngredientFromMenu;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistIngredientFromMenu;
import com.arwall.nosrecettes.persistence.model.IngredientFromMenuData;
import com.arwall.nosrecettes.persistence.repo.IngredientFromMenuRepository;

@Service
class IngredientFromMenuPersist implements PersistIngredientFromMenu {

    @Autowired
    private IngredientFromMenuRepository repository;

    @Override
    public Optional<IngredientFromMenu> findById(Long id) {
        return repository.findById(id).map(IngredientFromMenuData::toDomain);
    }

    @Override
    public List<IngredientFromMenu> retrieveAllIngredientFromMenu() {
        return repository.findAll().stream()
                .map(IngredientFromMenuData::toDomain).toList();
    }

    @Override
    public IngredientFromMenu update(IngredientFromMenu ingredient) {
        var ingredientFromMenuData = new IngredientFromMenuData(ingredient);
        return repository.save(ingredientFromMenuData).toDomain();
    }

    @Override
    public IngredientFromMenu create() {
        return repository.save(new IngredientFromMenuData()).toDomain();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
