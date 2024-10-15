package com.arwall.nosrecettes.domain.exception;

import lombok.Getter;

@Getter
public class RecipeNotFoundException extends ElementNotFoundException {

    public RecipeNotFoundException(Long id) {
        super(id);
    }

    @Override
    public String getErrorMessage() {
        return String.format("Recipe with id %s does not exist", id);
    }
}
