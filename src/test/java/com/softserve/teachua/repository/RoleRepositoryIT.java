package com.softserve.teachua.repository;

import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Slf4j
@DataJpaTest
class RoleRepositoryIT {
    private static final String EXISTING_NAME = "ROLE_ADMIN";
    private static final String NOT_EXISTING_NAME = "ROLE_CLIENT";
    private static final Integer RETURN_LIST_SIZE = 3;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByExistingNameShouldReturnCorrectRoleEntity() {
        assertThat(roleRepository.findByName(EXISTING_NAME).orElseThrow().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(roleRepository.findByName(NOT_EXISTING_NAME)).isEmpty();
    }

    @Test
    void findAllShouldReturnList() {
        assertThat(roleRepository.findAll()).hasSize(RETURN_LIST_SIZE);
        assertThat(roleRepository.findAll().get(0).getId()).isEqualTo(1);
        assertThat(roleRepository.findAll().get(1).getId()).isEqualTo(2);
        assertThat(roleRepository.findAll().get(2).getId()).isEqualTo(3);
    }

    @Test
    void existsByNameShouldReturnTrue() {
        assertTrue(roleRepository.existsByName(EXISTING_NAME));
    }

    @Test
    void notExistsByNameShouldReturnFalse() {
        assertFalse(roleRepository.existsByName(NOT_EXISTING_NAME));
    }
}
