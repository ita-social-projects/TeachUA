package com.softserve.teachua.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
public class ContactTypeRepositoryIT {

    private static final String EXISTING_NAME = "Телефон";
    private static final String NOT_EXISTING_NAME = "Telegram";

    @Autowired
    private ContactTypeRepository contactTypeRepository;

    @Test
    public void findByExistingNameShouldReturnCorrectContactTypeEntity() {
        assertThat(contactTypeRepository.findByName(EXISTING_NAME).get().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    public void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(contactTypeRepository.findByName(NOT_EXISTING_NAME)).isEqualTo(Optional.empty());
    }

    @Test
    public void existsByNameShouldReturnTrue() {
        assertTrue(contactTypeRepository.existsByName(EXISTING_NAME));
    }

    @Test
    public void notExistsByNameShouldReturnFalse() {
        assertFalse(contactTypeRepository.existsByName(NOT_EXISTING_NAME));
    }
}
