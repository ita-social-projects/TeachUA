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
class ContactTypeRepositoryIT {
    private static final String EXISTING_NAME = "Телефон";
    private static final String NOT_EXISTING_NAME = "Telegram";

    @Autowired
    private ContactTypeRepository contactTypeRepository;

    @Test
    void findByExistingNameShouldReturnCorrectContactTypeEntity() {
        assertThat(contactTypeRepository.findByName(EXISTING_NAME).orElseThrow().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(contactTypeRepository.findByName(NOT_EXISTING_NAME)).isEmpty();
    }

    @Test
    void existsByNameShouldReturnTrue() {
        assertTrue(contactTypeRepository.existsByName(EXISTING_NAME));
    }

    @Test
    void notExistsByNameShouldReturnFalse() {
        assertFalse(contactTypeRepository.existsByName(NOT_EXISTING_NAME));
    }
}
