package com.softserve.teachua.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.teachua.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);
}
