package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.city.CityProfile;
import com.softserve.teachua.dto.city.CityResponse;
import com.softserve.teachua.dto.city.SuccessCreatedCity;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.archivable.CenterArch;
import com.softserve.teachua.model.archivable.CityArch;
import com.softserve.teachua.repository.CityRepository;
import com.softserve.teachua.service.impl.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @InjectMocks
    private CityServiceImpl cityService;

    private City city;
    private CityProfile cityProfile;
    private CityResponse cityResponse;
    private CityArch cityArch;

    private final long EXISTING_ID = 1L;
    private final long NOT_EXISTING_ID = 500L;

    private final String NOT_EXISTING_NAME = "Kyiv";
    private final String EXISTING_NAME = "Lviv";
    private final String NEW_NAME = "Dnipro";

    @BeforeEach
    public void setUp() {
        city = City.builder().id(EXISTING_ID).name(EXISTING_NAME).build();
        cityProfile = CityProfile.builder().name(NEW_NAME).build();
        cityResponse = CityResponse.builder().name(EXISTING_NAME).build();
        cityArch = CityArch.builder().name(EXISTING_NAME).build();
    }

    @Test
    void getCityProfileByExistingIdShouldReturnCorrectCityProfile() {
        when(cityRepository.findById(EXISTING_ID)).thenReturn(Optional.of(city));
        when(dtoConverter.convertToDto(city, CityResponse.class))
                .thenReturn(CityResponse.builder().name(EXISTING_NAME).build());

        CityResponse actual = cityService.getCityProfileById(EXISTING_ID);
        assertEquals(city.getName(), actual.getName());
    }

    @Test
    void getCityProfileByNotExistingIdShouldThrowNotExistException() {
        when(cityRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            cityService.getCityProfileById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    public void getCityByExistingIdShouldReturnCorrectCity() {
        when(cityRepository.findById(EXISTING_ID)).thenReturn(Optional.of(city));

        City actual = cityService.getCityById(EXISTING_ID);
        assertEquals(city, actual);
    }

    @Test
    public void getCityByNotExistingIdShouldThrowNotExistException() {
        when(cityRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            cityService.getCityById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void getCityByExistingNameShouldReturnCorrectCity() {
        when(cityRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(city));

        City actual = cityService.getCityByName(EXISTING_NAME);
        assertEquals(city, actual);
    }

    @Test
    void getCityByNotExistingNameShouldThrowNotExistException() {
        when(cityRepository.findByName(NOT_EXISTING_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            cityService.getCityByName(NOT_EXISTING_NAME);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void addNewCityShouldReturnSuccessCreatedCity() {
        City newCity = City.builder().name(NEW_NAME).build();

        when(cityRepository.existsByName(NEW_NAME)).thenReturn(false);
        when(dtoConverter.convertToEntity(cityProfile, new City())).thenReturn(newCity);
        when(cityRepository.save(any())).thenReturn(newCity);
        when(dtoConverter.convertToDto(newCity, SuccessCreatedCity.class))
                .thenReturn(SuccessCreatedCity.builder().name(NEW_NAME).build());

        SuccessCreatedCity actual = cityService.addCity(cityProfile);
        assertEquals(cityProfile.getName(), actual.getName());
    }

    @Test
    void addExistingCityShouldThrowAlreadyExistException() {
        cityProfile.setName(EXISTING_NAME);
        when(cityRepository.existsByName(EXISTING_NAME)).thenReturn(true);

        assertThatThrownBy(() -> {
            cityService.addCity(cityProfile);
        }).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void getListOfCitiesShouldReturnListOfCitiResponces() {
        List<City> cities = Arrays.asList(city);

        when(dtoConverter.convertToDto(city, CityResponse.class)).thenReturn(cityResponse);
        when(cityRepository.findAllByOrderByIdAsc()).thenReturn(cities);

        List<CityResponse> actual = cityService.getListOfCities();
        assertEquals(cities.size(), actual.size());
        assertEquals(cities.get(0).getName(), actual.get(0).getName());
    }

    @Test
    void updateCityByExistingIdShouldReturnCorrectCityProfile() {
        when(cityRepository.findById(EXISTING_ID)).thenReturn(Optional.of(city));
        when(cityRepository.save(any())).thenReturn(city);
        when(dtoConverter.convertToEntity(cityProfile, city))
                .thenReturn(City.builder().name(cityProfile.getName()).build());
        when(dtoConverter.convertToDto(city, CityProfile.class)).thenReturn(cityProfile);

        CityProfile actual = cityService.updateCity(EXISTING_ID, cityProfile);
        assertEquals(cityProfile, actual);
    }

    @Test
    void updateCityByNotExistingIdShouldThrowNotExistException() {
        when(cityRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            cityService.updateCity(NOT_EXISTING_ID, cityProfile);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void deleteCityByExistingIdShouldReturnCityResponse() {
        when(cityRepository.findById(EXISTING_ID)).thenReturn(Optional.of(city));
        // when(archiveService.saveModel(city)).thenReturn(city);
        doNothing().when(cityRepository).deleteById(EXISTING_ID);
        doNothing().when(cityRepository).flush();
        when(dtoConverter.convertToDto(city, CityResponse.class))
                .thenReturn(CityResponse.builder().id(city.getId()).name(city.getName()).build());
        when(dtoConverter.convertToDto(city, CityArch.class)).thenReturn(cityArch);
        when(archiveService.saveModel(cityArch)).thenReturn(Archive.builder().build());
        CityResponse actual = cityService.deleteCityById(EXISTING_ID);
        assertEquals(city.getName(), actual.getName());
    }

    @Test
    void deleteCityByNotExistingIdShouldThrowNotExistException() {
        when(cityRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            cityService.deleteCityById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }
}