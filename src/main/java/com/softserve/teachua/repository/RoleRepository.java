package com.softserve.teachua.repository;

import com.softserve.teachua.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    List<Role> findAll();

    boolean existsByName(String name);
}
