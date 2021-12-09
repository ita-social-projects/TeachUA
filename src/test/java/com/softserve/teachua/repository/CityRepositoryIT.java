package com.softserve.teachua.repository;

import com.softserve.teachua.model.City;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
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
        List<City> cities = cityRepository.findAllByOrderByIdAsc();

        assertThat(cities.size()).isGreaterThan(0);

        for (int i = 1; i < cities.size(); i++) {
            assertThat(cities.get(i).getId()).isGreaterThan(cities.get(i - 1).getId());
        }
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
