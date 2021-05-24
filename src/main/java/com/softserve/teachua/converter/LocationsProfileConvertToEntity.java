package com.softserve.teachua.converter;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationsProfileConvertToEntity {

    private DtoConverter dtoConverter ;

    public LocationsProfileConvertToEntity (DtoConverter dtoConverter){
        this.dtoConverter = dtoConverter;
    }

    public Location convertFromLocationProfileToEntity(LocationProfile locationProfile){
        Location location = dtoConverter.convertToEntity(locationProfile, new Location());
        return null;
    }
}
