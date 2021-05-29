package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;


@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepository;
    private final DtoConverter dtoConverter;

    public LocationServiceImpl(LocationRepository locationRepository,
                               DtoConverter dtoConverter){
        this.locationRepository = locationRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public Location addLocation(LocationProfile locationProfile) {

        log.info(locationProfile.toString());
//        if(locationRepository.existsById(locationExcel.getId())){
//            throw new AlreadyExistException("this location already exist");
//        }
        Location location = null;
        try{
            location = locationRepository.save(dtoConverter.convertToEntity(locationProfile, new Location()));
        }catch(Exception e){
            log.info(e.getMessage());
        }
        return location;
    }

}
