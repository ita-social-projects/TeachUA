package com.softserve.teachua.converter;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.service.impl.LocationServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Data
@EqualsAndHashCode
@Slf4j

@Component
public class CoordinatesConverter {
    private LocationServiceImpl locationService;


    @Autowired
    public CoordinatesConverter(LocationServiceImpl locationService) {
        this.locationService = locationService;
    }

    public void LocationProfileConverterToDb(LocationProfile location){
        String coordinates = location.getCoordinates();
        String[] latAndLng = coordinates.replaceAll(" ","").split(",");
        location.setLatitude(Double.valueOf(latAndLng[0]));
        location.setLongitude(Double.valueOf(latAndLng[1]));

    }

    public void locationResponseConverterToDb(LocationResponse location){

        String coordinates = location.getCoordinates();
        String[] latAndLng = coordinates.replaceAll(" ","").split(",");
        location.setLatitude(Double.valueOf(latAndLng[0]));
        location.setLongitude(Double.valueOf(latAndLng[1]));

    }


}