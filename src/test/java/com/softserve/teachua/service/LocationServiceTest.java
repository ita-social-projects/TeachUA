package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    private static final Long CORRECT_LOCATION_ID = 1L;
    private static final Long CORRECT_CENTER_ID = 3L;
    private static final Long CORRECT_CLUB_ID = 5L;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private DtoConverter dtoConverter;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Mock
    private CityService cityService;

    @Mock
    private DistrictService districtService;

    @Mock
    private StationService stationService;

    private Center correctCenter;
    private Club correctClub;
    private Location correctLocation;
    private LocationProfile correctLocationProfile;
    private LocationResponse locationResponse;
    private Set<LocationProfile> filledLocationProfileSet;
    private Set<Location> locationSet;
    private Set<LocationResponse> locationResponses;

    @BeforeEach
    public void setMocks() {
        correctClub = Club.builder().id(CORRECT_CLUB_ID).locations(locationSet).build();

        correctCenter = Center.builder().id(CORRECT_CENTER_ID).locations(locationSet).build();

        correctLocation = Location.builder().id(CORRECT_LOCATION_ID).center(correctCenter).build();

        correctLocationProfile = LocationProfile.builder().id(CORRECT_LOCATION_ID).centerId(correctCenter.getId())
                .build();

        locationResponse = LocationResponse.builder().id(CORRECT_LOCATION_ID).build();

        filledLocationProfileSet = new HashSet<>();
        filledLocationProfileSet.add(correctLocationProfile);

        locationSet = new HashSet<>();
        locationSet.add(correctLocation);

        locationResponses = new HashSet<>();
        locationResponses.add(locationResponse);

    }

    @Test
    public void addLocationShouldReturnCreatedLocation() {
        when(dtoConverter.convertToEntity(correctLocationProfile, new Location())).thenReturn(correctLocation);
        when(locationRepository.save(correctLocation)).thenReturn(correctLocation);
        Location actual = locationService.addLocation(correctLocationProfile);
        assertThat(actual).isEqualTo(correctLocation);
    }

    @Test
    public void updateCenterLocationByFilledSetShouldReturnLocationSet() {
        when(locationRepository.deleteAllByCenter(correctCenter)).thenReturn(locationSet);
        when(dtoConverter.convertToEntity(correctLocationProfile, new Location()))
                .thenReturn(correctLocation);
        when(locationRepository.save(any(Location.class))).thenReturn(correctLocation);
        Set<Location> actual = locationService.updateCenterLocation(filledLocationProfileSet, correctCenter);
        assertThat(actual).isEqualTo(locationSet);
    }

    @Test
    public void updateCenterLocationByEmptyLocationProfileSetShouldReturnIncorrectInputException() {
        assertThatThrownBy(() -> locationService.updateCenterLocation(null, correctCenter)).isInstanceOf(IncorrectInputException.class);
    }

    @Test
    public void updateLocationByFilledClubShouldReturnLocationSet() {
        when(locationRepository.deleteAllByClub(correctClub)).thenReturn(locationSet);
        when(dtoConverter.convertToEntity(locationResponse, new Location()))
                .thenReturn(correctLocation);
        when(locationRepository.save(any(Location.class))).thenReturn(correctLocation);
        Set<Location> actual = locationService.updateLocationByClub(locationResponses, correctClub);
        assertThat(actual).isEqualTo(locationSet);
    }

    @Test
    public void updateLocationByEmptyClubShouldReturnNull() {
        Set<Location> expected = locationService.updateLocationByClub(null, correctClub);
        assertThat(expected).isEqualTo(null);
    }

}
