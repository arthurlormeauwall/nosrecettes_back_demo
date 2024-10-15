package com.arwall.nosrecettes.rest.model;

import com.arwall.nosrecettes.domain.model.Recipe;
import com.arwall.nosrecettes.domain.model.RecipeSummary;
import com.arwall.nosrecettes.domain.model.Season;

import lombok.Data;

@Data
public class RestRecipeSummary {

    private Long id;
    private String name;
    private String source;
    private Season season;
    private String type;

    public RestRecipeSummary(RecipeSummary recipe) {
        this.name = recipe.getName();
        this.season = recipe.getSeason();
        this.type = recipe.getType();
        this.source = recipe.getSource();
        this.id = recipe.getId();
    }

    public RestRecipeSummary() {
    }

    public Recipe toDomain() {
        return Recipe.builder()
                .withName(this.name)
                .withSeason(this.season)
                .withSource(this.source)
                .withType(this.type)
                .withId(this.id)
                .build();
    }
}
