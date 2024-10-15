package com.arwall.nosrecettes.domain.ddd;

import lombok.Getter;

@Getter
public abstract class DomainEntity {

    Long id;

    public DomainEntity(Long id) {
        this.id = id;
    }

    public Boolean isTheSameEntityAs(DomainEntity entity) {
        return id.equals(entity.getId());
    }
}
