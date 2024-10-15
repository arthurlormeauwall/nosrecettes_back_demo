package com.arwall.nosrecettes.domain.exception;

import lombok.Getter;

@Getter
public class IngredientNotFoundException extends ElementNotFoundException {

    public IngredientNotFoundException(Long id) {
        super(id);
    }

    @Override
    public String getErrorMessage() {
        return String.format("Ingredient with id %s does not exist", id);
    }
}

