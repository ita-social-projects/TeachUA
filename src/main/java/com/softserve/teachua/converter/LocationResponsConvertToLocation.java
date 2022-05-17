package com.softserve.teachua.converter;

import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationResponsConvertToLocation {

    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final StationRepository stationRepository;
    private final ClubRepository clubRepository;
    private final CenterRepository centerRepository;
    private final DtoConverter dtoConverter;


    @Autowired
    public LocationResponsConvertToLocation(CityRepository cityRepository, DistrictRepository districtRepository, StationRepository stationRepository, ClubRepository clubRepository, CenterRepository centerRepository, DtoConverter dtoConverter) {
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
        this.stationRepository = stationRepository;
        this.clubRepository = clubRepository;
        this.centerRepository = centerRepository;
        this.dtoConverter = dtoConverter;
    }

//    public Location locationResponseConvertToEntityLocation (LocationResponse locationResponse, Location location){
//        Location locationUpdate = new Location();
//        City city =dtoConverter.convertToEntity(locationResponse.getCity(),new City());
//        Club club = clubRepository.getOne(locationResponse.getClubId());
//        locationUpdate.setCity(city);
//        locationUpdate.setId(locationResponse.getId());
//        locationUpdate.setAddress(locationResponse.getAddress());
//        locationUpdate.setLatitude(locationResponse.getLatitude());
//        locationUpdate.setLongitude(locationResponse.getLongitude());
//        locationUpdate.setPhone(locationResponse.getPhone());
//        locationUpdate.setDistrict(districtRepository.getOne(locationResponse.getDistrictId()));
//        locationUpdate.setStation(stationRepository.getOne(locationResponse.getStationId()));
//        locationUpdate.setClub(club);
//        locationUpdate.setCenter(club.getCenter());
//
//        return locationUpdate;
//    }
}
