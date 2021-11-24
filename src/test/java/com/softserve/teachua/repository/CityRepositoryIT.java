package com.softserve.teachua.repository;

import com.softserve.teachua.model.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
public class CityRepositoryIT {
    private static final String EXISTING_NAME_1 = "City 1";
    private static final String EXISTING_NAME_2 = "City 2";
    private static final String EXISTING_NAME_3 = "City 3";
    private static final String EXISTING_NAME_4 = "City 4";

    private static final String NOT_EXISTING_NAME = "Not Existing City";

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    private static final Double COORDINATES = 0.0;

    private final City city1 = City.builder()
            .name(EXISTING_NAME_1)
            .latitude(COORDINATES)
            .longitude(COORDINATES)
            .build();
    private final City city2 = City.builder()
            .name(EXISTING_NAME_2)
            .latitude(COORDINATES)
            .longitude(COORDINATES)
            .build();
    private final City city3 = City.builder()
            .name(EXISTING_NAME_3)
            .latitude(COORDINATES)
            .longitude(COORDINATES)
            .build();
    private final City city4 = City.builder()
            .name(EXISTING_NAME_4)
            .latitude(COORDINATES)
            .longitude(COORDINATES)
            .build();

    private final List<City> cities = Arrays.asList(city1, city2, city3, city4);

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    public void setUp() {
        entityManager.persist(city1);
        entityManager.persist(city2);
        entityManager.persist(city3);
        entityManager.persist(city4);
    }

    @Test
    public void findByExistingIdShouldReturnCorrectCityEntity() {
        assertThat(cityRepository.findById(EXISTING_ID)).isEqualTo(Optional.of(city1));
    }

    @Test
    public void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(cityRepository.findById(NOT_EXISTING_ID)).isEqualTo(Optional.empty());
    }

    @Test
    public void findAllByOrderByIdAscShouldReturnSortedListByIdAsc() {
        assertThat(cityRepository.findAllByOrderByIdAsc()).isEqualTo(cities);
    }

    @Test
    public void findByExistingNameShouldReturnCorrectCityEntity() {
        assertThat(cityRepository.findByName(EXISTING_NAME_3)).isEqualTo(Optional.of(city3));
    }

    @Test
    public void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(cityRepository.findByName(NOT_EXISTING_NAME)).isEqualTo(Optional.empty());
    }

    @Test
    public void existingByExistingNameShouldReturnTrue(){
        assertTrue(cityRepository.existsByName(EXISTING_NAME_2));
    }

    @Test
    public void existingByNotExistingNameShouldReturnFalse(){
        assertFalse(cityRepository.existsByName(NOT_EXISTING_NAME));
    }
}
