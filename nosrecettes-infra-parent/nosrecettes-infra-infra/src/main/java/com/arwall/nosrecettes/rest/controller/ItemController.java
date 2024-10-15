package com.arwall.nosrecettes.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arwall.nosrecettes.domain.gatesToInfra.api.ItemCrudOperations;
import com.arwall.nosrecettes.rest.model.RestItem;

@RestController
@CrossOrigin
@RequestMapping("/item")
public class ItemController {

    private final ItemCrudOperations itemCrudOperations;

    @Autowired
    public ItemController(ItemCrudOperations itemCrudOperations) {
        this.itemCrudOperations=itemCrudOperations;
    }

    @GetMapping
    public List<RestItem> findAll() {
        return itemCrudOperations.findAll().stream().map(recipe -> new RestItem(recipe)).toList();
    }

    @GetMapping("/{id}")
    public RestItem findById(@PathVariable Long id) throws Exception {
        return new RestItem(itemCrudOperations.retrieve(id));
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public RestItem update(@RequestBody RestItem restItem) {
        return new RestItem(itemCrudOperations.update(restItem.toDomain()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public void delete(@RequestParam long id) {
        itemCrudOperations.delete(id);
    }
}
