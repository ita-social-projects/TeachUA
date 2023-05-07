package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.district.DistrictProfile;
import com.softserve.teachua.dto.district.DistrictResponse;
import com.softserve.teachua.dto.district.SuccessCreatedDistrict;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.District;
import com.softserve.teachua.repository.DistrictRepository;
import com.softserve.teachua.service.impl.DistrictServiceImpl;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DistrictServiceTest {

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @Mock
    private CityService cityService;

    @Mock
    private DistrictRepository districtRepository;

    @InjectMocks
    private DistrictServiceImpl districtService;

    private District district;
    private City city;
    private DistrictResponse districtResponse;
    private SuccessCreatedDistrict successCreatedDistrict;
    private DistrictProfile districtProfile;

    // district
    private final Long CORRECT_DISTRICT_ID = 1L;
    private final Long WRONG_DISTRICT_ID = 2L;
    private final String CORRECT_DISTRICT_NAME = "MyDistrict";
    private final String WRONG_DISTRICT_NAME = "BadDistrict";

    // city
    private final Long CORRECT_CITY_ID = 3L;
    private final String CITY_NAME = "MyCity";
    private final String WRONG_CITY_NAME = "BadCity";
    private final Double CITY_LATITUDE = 1.11D;
    private final Double CITY_LONGITUDE = 2.22D;

    @BeforeEach
    public void setMocks() {
        city = City.builder().id(CORRECT_CITY_ID).name(CITY_NAME).latitude(CITY_LATITUDE).longitude(CITY_LONGITUDE)
                .build();

        district = District.builder().id(CORRECT_DISTRICT_ID).name(CORRECT_DISTRICT_NAME).city(city).build();

        districtResponse = DistrictResponse.builder().id(CORRECT_DISTRICT_ID).name(CORRECT_DISTRICT_NAME)
                .cityName(CITY_NAME).build();

        successCreatedDistrict = SuccessCreatedDistrict.builder().id(CORRECT_DISTRICT_ID).name(CORRECT_DISTRICT_NAME)
                .cityName(CITY_NAME).build();

        districtProfile = DistrictProfile.builder().name(CORRECT_DISTRICT_NAME).cityName(CITY_NAME).build();
    }

    @Test
    public void getDistrictByCorrectIdShouldReturnDistrict() {
        when(districtRepository.findById(CORRECT_DISTRICT_ID)).thenReturn(Optional.of(district));
        District actual = districtService.getDistrictById(CORRECT_DISTRICT_ID);
        assertThat(actual).isEqualTo(district);
    }

    @Test
    public void getDistrictByWrongIdShouldReturnNull() {
        when(districtRepository.findById(WRONG_DISTRICT_ID)).thenReturn(Optional.empty());
        District actual = districtService.getDistrictById(WRONG_DISTRICT_ID);
        assertThat(actual).isEqualTo(null);
    }

    @Test
    public void getDistrictProfileByIdShouldReturnDistrictResponse() {
        when(districtRepository.findById(CORRECT_DISTRICT_ID)).thenReturn(Optional.of(district));
        when(dtoConverter.convertToDto(district, DistrictResponse.class)).thenReturn(districtResponse);
        assertThat(districtService.getDistrictProfileById(CORRECT_DISTRICT_ID)).isEqualTo(districtResponse);
    }

    @Test
    public void getDistrictByCorrectNameShouldReturnDistrict() {
        when(districtRepository.findFirstByName(CORRECT_DISTRICT_NAME)).thenReturn(Optional.of(district));
        assertThat(districtService.getDistrictByName(CORRECT_DISTRICT_NAME)).isEqualTo(district);
    }

    @Test
    public void getDistrictByWrongNameShouldThrowNotExistException() {
        when(districtRepository.findFirstByName(WRONG_DISTRICT_NAME)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> districtService.getDistrictByName(WRONG_DISTRICT_NAME)).isInstanceOf(NotExistException.class);

    }

    @Test
    public void addDistrictShouldReturnSuccessCreatedDistrict() {
        when(districtRepository.existsByName(CORRECT_DISTRICT_NAME)).thenReturn(false);
        when(cityService.getCityByName(CITY_NAME)).thenReturn(city);
        when(dtoConverter.convertToEntity(districtProfile, new District())).thenReturn(district);
        when(districtRepository.save(district)).thenReturn(district);
        when(dtoConverter.convertToDto(district, SuccessCreatedDistrict.class)).thenReturn(successCreatedDistrict);
        assertThat(districtService.addDistrict(districtProfile)).isEqualTo(successCreatedDistrict);
    }

    @Test
    public void addDistrictShouldThrowAlreadyExistException() {
        when(districtRepository.existsByName(CORRECT_DISTRICT_NAME)).thenReturn(true);
        assertThatThrownBy(() -> districtService.addDistrict(districtProfile)).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    public void getListOfDistrictsShouldReturnListOfDistrictResponses() {
        when(districtRepository.findAllByOrderByIdAsc()).thenReturn(Collections.singletonList(district));
        when(dtoConverter.convertToDto(district, DistrictResponse.class)).thenReturn(districtResponse);

        assertThat(districtService.getListOfDistricts()).isNotEmpty().isEqualTo(
                Collections.singletonList(districtResponse));
    }

    @Test
    public void getListOfDistrictsByCorrectCityNameShouldReturnListOfDistrictResponses() {
        when(districtRepository.findAllByCityName(CITY_NAME)).thenReturn(Collections.singletonList(district));
        when(dtoConverter.convertToDto(district, DistrictResponse.class)).thenReturn(districtResponse);

        assertThat(districtService.getListOfDistrictsByCityName(CITY_NAME)).isNotEmpty()
                .isEqualTo(Collections.singletonList(districtResponse));
    }

    @Test
    public void getListOfDistrictsByWrongCityNameShouldReturnListOfDistrictResponses() {
        when(districtRepository.findAllByCityName(WRONG_CITY_NAME)).thenReturn(List.of());
        assertThat(districtService.getListOfDistrictsByCityName(WRONG_CITY_NAME)).isEmpty();
    }

}
