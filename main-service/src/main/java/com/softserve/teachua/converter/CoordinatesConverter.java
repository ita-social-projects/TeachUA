package com.softserve.teachua.converter;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.exception.IncorrectInputException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Slf4j
@Component
public class CoordinatesConverter {
    public void locationProfileConverterToDb(LocationProfile location) {
        if (location != null && location.getCoordinates() != null) {
            double[] latitudeAndLongitude = parseLatitudeAndLongitude(location.getCoordinates());
            location.setLatitude(latitudeAndLongitude[0]);
            location.setLongitude(latitudeAndLongitude[1]);
        }
    }

    private double[] parseLatitudeAndLongitude(String coordinates) {
        try {
            String[] latAndLng = coordinates.replace(" ", "").split(",");
            return new double[]{Double.parseDouble(latAndLng[0]), Double.parseDouble(latAndLng[1])};
        } catch (Exception e) {
            throw new IncorrectInputException("Parsing error. Wrong coordinates format.");
        }
    }
}
