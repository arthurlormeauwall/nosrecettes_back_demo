package com.arwall.nosrecettes.persistence.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.arwall.nosrecettes.persistence.model.MenuData;


// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface MenuRepository extends JpaRepository<MenuData, Long> {

}