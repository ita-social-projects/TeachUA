package com.softserve.teachua.converter;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationsProfileConvertToEntity {

    private DtoConverter dtoConverter ;

    @Autowired
    public LocationsProfileConvertToEntity (DtoConverter dtoConverter){
        this.dtoConverter = dtoConverter;
    }

    public Location convertFromLocationProfileToEntity(LocationProfile locationProfile){
        Location location = dtoConverter.convertToEntity(locationProfile, new Location());
        return null;
    }
}
