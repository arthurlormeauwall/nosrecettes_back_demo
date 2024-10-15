package com.arwall.nosrecettes.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arwall.nosrecettes.persistence.model.ItemData;

public interface ItemRepository extends JpaRepository<ItemData, Long> {

}
