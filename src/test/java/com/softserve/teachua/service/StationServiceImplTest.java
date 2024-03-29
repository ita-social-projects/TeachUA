package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.station.StationProfile;
import com.softserve.teachua.dto.station.StationResponse;
import com.softserve.teachua.dto.station.SuccessCreatedStation;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.Station;
import com.softserve.teachua.repository.StationRepository;
import com.softserve.teachua.service.impl.StationServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StationServiceImplTest {
    // station
    private final Long CORRECT_STATION_ID = 1L;
    private final Long WRONG_STATION_ID = 2L;
    private final String CORRECT_STATION_NAME = "MyStation";
    private final String WRONG_STATION_NAME = "BadStation";
    // city
    private final Long CORRECT_CITY_ID = 3L;
    private final String CITY_NAME = "MyCity";
    private final String WRONG_CITY_NAME = "BadCity";
    private final Double CITY_LATITUDE = 1.11D;
    private final Double CITY_LONGITUDE = 2.22D;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private CityService cityService;
    @Mock
    private DistrictService districtService;
    @Mock
    private ArchiveService archiveService;
    @InjectMocks
    private StationServiceImpl stationService;
    private StationResponse stationResponse;
    private Station station;
    private StationProfile stationProfile;
    private City city;
    private SuccessCreatedStation successCreatedStation;

    @BeforeEach
    void setMocks() {
        city = City.builder().id(CORRECT_CITY_ID).name(CITY_NAME).latitude(CITY_LATITUDE).longitude(CITY_LONGITUDE)
                .build();

        station = Station.builder().id(CORRECT_STATION_ID).name(CORRECT_STATION_NAME).city(city).build();

        stationResponse = StationResponse.builder().id(CORRECT_STATION_ID).name(CORRECT_STATION_NAME)
                .cityName(CITY_NAME).build();

        stationProfile = StationProfile.builder().name(CORRECT_STATION_NAME).cityName(CITY_NAME).build();

        successCreatedStation = SuccessCreatedStation.builder().id(CORRECT_STATION_ID).name(CORRECT_STATION_NAME)
                .cityName(CITY_NAME).build();
    }

    @Test
    void getStationByCorrectIdShouldReturnStation() {
        when(stationRepository.findById(CORRECT_STATION_ID)).thenReturn(Optional.of(station));
        Station actual = stationService.getStationById(CORRECT_STATION_ID);
        assertThat(actual).isEqualTo(station);
    }

    @Test
    void getStationByWrongIdShouldReturnNull() {
        when(stationRepository.findById(WRONG_STATION_ID)).thenReturn(Optional.empty());
        Station actual = stationService.getStationById(WRONG_STATION_ID);
        assertThat(actual).isNull();
    }

    @Test
    void getStationProfileByIdShouldReturnStationResponse() {
        when(stationRepository.findById(CORRECT_STATION_ID)).thenReturn(Optional.of(station));
        when(dtoConverter.convertToDto(station, StationResponse.class)).thenReturn(stationResponse);
        StationResponse actual = stationService.getStationProfileById(CORRECT_STATION_ID);
        assertThat(actual).isEqualTo(stationResponse);
    }

    @Test
    void getStationByCorrectNameShouldReturnStation() {
        when(stationRepository.findByName(CORRECT_STATION_NAME)).thenReturn(Optional.of(station));
        Station actual = stationService.getStationByName(CORRECT_STATION_NAME);
        assertThat(actual).isEqualTo(station);
    }

    @Test
    void getStationByWrongNameShouldThrowNotExistException() {
        when(stationRepository.findByName(WRONG_STATION_NAME)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> stationService.getStationByName(WRONG_STATION_NAME)).isInstanceOf(
                NotExistException.class);
    }

    @Test
    void addStationShouldReturnSuccessCreatedStation() {
        when(stationRepository.existsByName(CORRECT_STATION_NAME)).thenReturn(false);
        when(cityService.getCityByName(CITY_NAME)).thenReturn(city);
        when(dtoConverter.convertToEntity(stationProfile, new Station())).thenReturn(station);
        when(stationRepository.save(station)).thenReturn(station);
        when(dtoConverter.convertToDto(station, SuccessCreatedStation.class)).thenReturn(successCreatedStation);
        assertThat(stationService.addStation(stationProfile)).isEqualTo(successCreatedStation);
    }

    @Test
    void addStationShouldThrowAlreadyExistException() {
        when(stationRepository.existsByName(CORRECT_STATION_NAME)).thenReturn(true);
        assertThatThrownBy(() -> stationService.addStation(stationProfile)).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void getListOfStationsShouldReturnListOfStationResponses() {
        when(stationRepository.findAll()).thenReturn(Collections.singletonList(station));
        when(dtoConverter.convertToDto(station, StationResponse.class)).thenReturn(stationResponse);

        assertThat(stationService.getListOfStations()).isNotEmpty().isEqualTo(
                Collections.singletonList(stationResponse));
    }

    @Test
    void getListOfStationsByCorrectCityNameShouldReturnListOfStationResponses() {
        when(stationRepository.findAllByCityName(CITY_NAME)).thenReturn(Collections.singletonList(station));
        when(dtoConverter.convertToDto(station, StationResponse.class)).thenReturn(stationResponse);

        assertThat(stationService.getListOfStationsByCityName(CITY_NAME)).isNotEmpty()
                .isEqualTo(Collections.singletonList(stationResponse));
    }

    @Test
    void getListOfStationsByWrongCityNameShouldReturnListOfStationResponses() {
        when(stationRepository.findAllByCityName(WRONG_CITY_NAME)).thenReturn(List.of());
        assertThat(stationService.getListOfStationsByCityName(WRONG_CITY_NAME)).isEmpty();
    }
}
