package com.softserve.teachua.repository;

import com.softserve.teachua.model.District;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DistrictRepositoryIT {
    private static final Long EXISTING_ID = 1L;
    private static final Long NOT_EXISTING_ID = 500L;

    private static final String EXISTING_NAME = "Дарницький";
    private static final String NOT_EXISTING_NAME = "Франківський";

    private static final String EXISTING_CITY_NAME = "Київ";
    private static final String NOT_EXISTING_CITY_NAME = "Миколаїв";

    @Autowired
    private DistrictRepository districtRepository;

    @Test
    void findByExistingIdShouldReturnOptionalOfCorrectDistrictEntity() {
        assertThat(districtRepository.findById(EXISTING_ID)).isInstanceOf(Optional.class);
        assertThat(districtRepository.findById(EXISTING_ID).orElseThrow()).isInstanceOf(District.class);
        assertThat(districtRepository.findById(EXISTING_ID).orElseThrow().getId()).isEqualTo(EXISTING_ID);
    }

    @Test
    void findByNotExistingIdShouldReturnOptionalEmpty() {
        assertThat(districtRepository.findById(NOT_EXISTING_ID)).isEmpty();
    }

    @Test
    void findByExistingNameShouldReturnOptionalOfCorrectDistrictEntity() {
        assertThat(districtRepository.findByName(EXISTING_NAME)).isInstanceOf(Optional.class);
        assertThat(districtRepository.findByName(EXISTING_NAME).orElseThrow()).isInstanceOf(District.class);
        assertThat(districtRepository.findByName(EXISTING_NAME).orElseThrow().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    void findByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(districtRepository.findByName(NOT_EXISTING_NAME)).isEmpty();
    }

    @Test
    void findFirstByExistingNameShouldReturnOptionalOfCorrectDistrictEntity() {
        assertThat(districtRepository.findByName(EXISTING_NAME)).isInstanceOf(Optional.class);
        assertThat(districtRepository.findByName(EXISTING_NAME).orElseThrow()).isInstanceOf(District.class);
        assertThat(districtRepository.findByName(EXISTING_NAME).orElseThrow().getName()).isEqualTo(EXISTING_NAME);
    }

    @Test
    void findFirstByNotExistingNameShouldReturnOptionalEmpty() {
        assertThat(districtRepository.findByName(NOT_EXISTING_NAME)).isEmpty();
    }

    @Test
    void existsByExistingNameShouldReturnTrue() {
        assertTrue(districtRepository.existsByName(EXISTING_NAME));
    }

    @Test
    void existsByNotExistingNameShouldReturnFalse() {
        assertFalse(districtRepository.existsByName(NOT_EXISTING_NAME));
    }

    @Test
    void findAllByExistingCityNameShouldReturnListOfCorrectDistrictEntities() {
        List<District> list = districtRepository.findAllByCityName(EXISTING_CITY_NAME);

        assertThat(list).isNotEmpty();

        for (District district : list) {
            assertThat(district.getCity().getName()).isEqualTo(EXISTING_CITY_NAME);
        }
    }

    @Test
    void findAllByNotExistingCityNameShouldReturnEmptyList() {
        assertThat(districtRepository.findAllByCityName(NOT_EXISTING_CITY_NAME)).isEmpty();
    }

    @Test
    void findAllByOrderByIdAscTest() {
        List<District> list = districtRepository.findAllByOrderByIdAsc();

        assertThat(list).isNotEmpty();

        for (int i = 1; i < list.size(); i++) {
            assertThat(list.get(i).getId()).isGreaterThan(list.get(i - 1).getId());
        }
    }
}
