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
public class CityRepositoryIT {
    private static final String EXISTING_NAME = "Одеса";
    private static final String NOT_EXISTING_NAME = "Івано-Франківськ";

    private static final Long EXISTING_ID = 4L;
    private static final Long NOT_EXISTING_ID = 100L;

    private static final Integer RETURN_LIST_SIZE = 9;

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void findByExistingIdShouldReturnCorrectCityEntity() {
        assertThat(cityRepository.findById(EXISTING_ID).get().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    public void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(cityRepository.findById(NOT_EXISTING_ID)).isEqualTo(Optional.empty());
    }

    @Test
    public void findAllByOrderByIdAscShouldReturnSortedListByIdAsc() {
        assertThat(cityRepository.findAllByOrderByIdAsc()).hasSize(RETURN_LIST_SIZE);
        assertThat(cityRepository.findAllByOrderByIdAsc().get(0).getId()).isEqualTo(1L);
        assertThat(cityRepository.findAllByOrderByIdAsc().get(1).getId()).isEqualTo(2L);
        assertThat(cityRepository.findAllByOrderByIdAsc().get(2).getId()).isEqualTo(3L);
    }

    @Test
    public void findByExistingNameShouldReturnCorrectCityEntity() {
        assertThat(cityRepository.findByName(EXISTING_NAME).get().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    public void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(cityRepository.findByName(NOT_EXISTING_NAME)).isEqualTo(Optional.empty());
    }

    @Test
    public void existingByExistingNameShouldReturnTrue() {
        assertTrue(cityRepository.existsByName(EXISTING_NAME));
    }

    @Test
    public void existingByNotExistingNameShouldReturnFalse() {
        assertFalse(cityRepository.existsByName(NOT_EXISTING_NAME));
    }
}
