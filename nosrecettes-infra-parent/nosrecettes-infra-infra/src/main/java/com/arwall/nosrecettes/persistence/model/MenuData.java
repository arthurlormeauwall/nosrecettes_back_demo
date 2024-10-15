package com.arwall.nosrecettes.persistence.model;

import java.util.ArrayList;
import java.util.List;

import com.arwall.nosrecettes.domain.model.Menu;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MenuData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    private List<Long> recipesId;

    public MenuData(Menu menu) {
        this.id = menu.getId();
        this.recipesId = menu.getRecipeIds();
    }

    public MenuData() {
        this.recipesId = new ArrayList<>();
    }

    public Menu toDomain() {
        return Menu.builder().withId(this.id).withRecipeIds(this.recipesId).build();
    }
}
