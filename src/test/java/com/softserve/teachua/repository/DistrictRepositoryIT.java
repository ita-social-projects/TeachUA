package com.softserve.teachua.repository;

import com.softserve.teachua.model.District;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class DistrictRepositoryIT {
    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 500L;

    private static final String EXISTING_NAME = "Дарницький";
    private static final String NOT_EXISTING_NAME = "Франківський";

    private static final String EXISTING_CITY_NAME = "Київ";
    private static final String NOT_EXISTING_CITY_NAME = "Миколаїв";

    @Autowired
    private DistrictRepository districtRepository;

    @Test
    public void findByExistingIdShouldReturnOptionalOfCorrectDistrictEntity() {
        assertThat(districtRepository.findById(EXISTING_ID)).isInstanceOf(Optional.class);
        assertThat(districtRepository.findById(EXISTING_ID).get()).isInstanceOf(District.class);
        assertThat(districtRepository.findById(EXISTING_ID).get().getId()).isEqualTo(EXISTING_ID);
    }

    @Test
    public void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(districtRepository.findById(NOT_EXISTING_ID)).isEqualTo(Optional.empty());
    }

    @Test
    public void findByExistingNameShouldReturnOptionalOfCorrectDistrictEntity() {
        assertThat(districtRepository.findByName(EXISTING_NAME)).isInstanceOf(Optional.class);
        assertThat(districtRepository.findByName(EXISTING_NAME).get()).isInstanceOf(District.class);
        assertThat(districtRepository.findByName(EXISTING_NAME).get().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    public void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(districtRepository.findByName(NOT_EXISTING_NAME)).isEqualTo(Optional.empty());
    }

    @Test
    public void findFirstByExistingNameShouldReturnOptionalOfCorrectDistrictEntity() {
        assertThat(districtRepository.findByName(EXISTING_NAME)).isInstanceOf(Optional.class);
        assertThat(districtRepository.findByName(EXISTING_NAME).get()).isInstanceOf(District.class);
        assertThat(districtRepository.findByName(EXISTING_NAME).get().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    public void findFirstByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(districtRepository.findByName(NOT_EXISTING_NAME)).isEqualTo(Optional.empty());
    }

    @Test
    public void existsByExistingNameShouldReturnTrue() {
        assertTrue(districtRepository.existsByName(EXISTING_NAME));
    }

    @Test
    public void existsByNotExistingNameShouldReturnFalse() {
        assertFalse(districtRepository.existsByName(NOT_EXISTING_NAME));
    }

    @Test
    public void findAllByExistingCityNameShouldReturnListOfCorrectDistrictEntities() {
        List<District> list = districtRepository.findAllByCityName(EXISTING_CITY_NAME);

        assertThat(list.size()).isGreaterThan(0);

        for (District district : list) {
            assertThat(district.getCity().getName()).isEqualTo(EXISTING_CITY_NAME);
        }
    }

    @Test
    public void findAllByNotExistingCityNameShouldReturnEmptyList() {
        assertThat(districtRepository.findAllByCityName(NOT_EXISTING_CITY_NAME).size()).isEqualTo(0);
    }

    @Test
    public void findAllByOrderByIdAscTest() {
        List<District> list = districtRepository.findAllByOrderByIdAsc();

        assertThat(list.size()).isGreaterThan(0);

        for (int i = 1; i < list.size(); i++) {
            assertThat(list.get(i).getId()).isGreaterThan(list.get(i - 1).getId());
        }
    }
}
