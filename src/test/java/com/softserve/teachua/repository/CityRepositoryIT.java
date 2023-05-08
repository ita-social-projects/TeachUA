package com.softserve.teachua.repository;

import com.softserve.teachua.model.City;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Slf4j
@DataJpaTest
class CityRepositoryIT {
    private static final String EXISTING_NAME = "Одеса";
    private static final String NOT_EXISTING_NAME = "Івано-Франківськ";

    private static final Long EXISTING_ID = 4L;
    private static final Long NOT_EXISTING_ID = 100L;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void findByExistingIdShouldReturnCorrectCityEntity() {
        assertThat(cityRepository.findById(EXISTING_ID).orElseThrow().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(cityRepository.findById(NOT_EXISTING_ID)).isEmpty();
    }

    @Test
    void findAllByOrderByIdAscShouldReturnSortedListByIdAsc() {
        List<City> cities = cityRepository.findAllByOrderByIdAsc();

        assertThat(cities).isNotEmpty();

        for (int i = 1; i < cities.size(); i++) {
            assertThat(cities.get(i).getId()).isGreaterThan(cities.get(i - 1).getId());
        }
    }

    @Test
    void findByExistingNameShouldReturnCorrectCityEntity() {
        assertThat(cityRepository.findByName(EXISTING_NAME).orElseThrow().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(cityRepository.findByName(NOT_EXISTING_NAME)).isEmpty();
    }

    @Test
    void existingByExistingNameShouldReturnTrue() {
        assertTrue(cityRepository.existsByName(EXISTING_NAME));
    }

    @Test
    void existingByNotExistingNameShouldReturnFalse() {
        assertFalse(cityRepository.existsByName(NOT_EXISTING_NAME));
    }
}
