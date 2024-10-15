package com.arwall.nosrecettes.rest.exceptionhandler;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.arwall.nosrecettes.domain.exception.ElementNotFoundException;
import com.arwall.nosrecettes.domain.exception.IngredientNotFoundException;
import com.arwall.nosrecettes.domain.exception.RecipeNotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public static Exception handleException(ElementNotFoundException exception) {
        if (exception instanceof RecipeNotFoundException) {
            return getRecipeNotFound(exception, "Recipe not found");
        }
        if (exception instanceof IngredientNotFoundException) {
            return getRecipeNotFound(exception, "Ingredient not found");
        }
        return exception;
    }

    private static ResponseStatusException getRecipeNotFound(ElementNotFoundException exception,
            String recipeNotFound) {
        var errorMessage = exception.getErrorMessage();
        LoggerFactory.getLogger(ControllerExceptionHandler.class).error(errorMessage);
        return new ResponseStatusException(HttpStatus.NOT_FOUND, recipeNotFound, exception);
    }
}
