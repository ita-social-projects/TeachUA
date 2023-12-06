package com.softserve.teachua.repository;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Slf4j
@DataJpaTest
class StationRepositoryIT {
    private static final String EXISTING_NAME = "Академмістечко";
    private static final String NOT_EXISTING_NAME = "Кульпарова";

    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 100L;

    private static final String EXISTING_CITY_NAME = "Київ";
    private static final String NOT_EXISTING_CITY_NAME = "Тернопіль";

    private static final Integer RETURN_LIST_SIZE = 11;

    @Autowired
    private StationRepository stationRepository;

    @Test
    void findByExistingIdShouldReturnCorrectStationEntity() {
        assertThat(stationRepository.findById(EXISTING_ID).orElseThrow().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(stationRepository.findById(NOT_EXISTING_ID)).isEmpty();
    }

    @Test
    void findAllByExistingCityNameShouldReturnListOfStations() {
        assertThat(stationRepository.findAllByCityName(EXISTING_CITY_NAME)).hasSize(RETURN_LIST_SIZE);
        assertThat(stationRepository.findAll().get(0).getId()).isEqualTo(1L);
        assertThat(stationRepository.findAll().get(1).getId()).isEqualTo(2L);
        assertThat(stationRepository.findAll().get(2).getId()).isEqualTo(3L);
        assertThat(stationRepository.findAll().get(3).getId()).isEqualTo(4L);
        assertThat(stationRepository.findAll().get(4).getId()).isEqualTo(5L);
        assertThat(stationRepository.findAll().get(5).getId()).isEqualTo(6L);
        assertThat(stationRepository.findAll().get(6).getId()).isEqualTo(7L);
        assertThat(stationRepository.findAll().get(7).getId()).isEqualTo(8L);
        assertThat(stationRepository.findAll().get(8).getId()).isEqualTo(9L);
        assertThat(stationRepository.findAll().get(9).getId()).isEqualTo(10L);
        assertThat(stationRepository.findAll().get(10).getId()).isEqualTo(11L);
    }

    @Test
    void findAllByNotExistingCityNameShouldReturnEmptyListOfStations() {
        assertThat(stationRepository.findAllByCityName(NOT_EXISTING_CITY_NAME)).isEqualTo(Collections.emptyList());
    }

    @Test
    void findByExistingNameShouldReturnCorrectStationEntity() {
        assertThat(stationRepository.findByName(EXISTING_NAME).orElseThrow().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(stationRepository.findByName(NOT_EXISTING_NAME)).isEmpty();
    }

    @Test
    void existsByNameShouldReturnTrue() {
        assertTrue(stationRepository.existsByName(EXISTING_NAME));
    }

    @Test
    void notExistsByNameShouldReturnFalse() {
        assertFalse(stationRepository.existsByName(NOT_EXISTING_NAME));
    }
}
