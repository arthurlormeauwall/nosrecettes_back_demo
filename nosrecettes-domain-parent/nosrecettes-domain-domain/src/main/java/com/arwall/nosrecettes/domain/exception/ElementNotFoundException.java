package com.arwall.nosrecettes.domain.exception;

public abstract class ElementNotFoundException extends  IllegalArgumentException{
    protected final Long id;

    public ElementNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public abstract String getErrorMessage();
}
