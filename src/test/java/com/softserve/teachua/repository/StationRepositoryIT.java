package com.softserve.teachua.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
public class StationRepositoryIT {

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
    public void findByExistingIdShouldReturnCorrectStationEntity() {
        assertThat(stationRepository.findById(EXISTING_ID).get().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    public void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(stationRepository.findById(NOT_EXISTING_ID)).isEqualTo(Optional.empty());
    }

    @Test
    public void findAllByExistingCityNameShouldReturnListOfStations(){
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
    public void findAllByNotExistingCityNameShouldReturnEmptyListOfStations(){
        assertThat(stationRepository.findAllByCityName(NOT_EXISTING_CITY_NAME)).isEqualTo(Collections.emptyList());
    }

    @Test
    public void findByExistingNameShouldReturnCorrectStationEntity(){
        assertThat(stationRepository.findByName(EXISTING_NAME).get().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    public void findByNotExistingNameShouldReturnOptionalEmpty(){
        assertThat(stationRepository.findByName(NOT_EXISTING_NAME)).isEqualTo(Optional.empty());
    }

    @Test
    public void existsByNameShouldReturnTrue(){
        assertTrue(stationRepository.existsByName(EXISTING_NAME));
    }

    @Test
    public void notExistsByNameShouldReturnFalse(){
        assertFalse(stationRepository.existsByName(NOT_EXISTING_NAME));
    }




}
