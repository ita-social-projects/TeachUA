package com.softserve.teachua.service;

import com.softserve.teachua.dto.city.CityResponse;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import java.util.Set;

/**
 * This interface contains all needed methods to manage locations.
 */

public interface LocationService {
    /**
     *The method returns {@code Location} entity.
     *
     * @param locationProfile - place body of dto {@code LocationProfile}.
     * @return new {@code Location}.
     */
    Location addLocation(LocationProfile locationProfile);

    /**
     *  The method returns updated {@code Set<Location>} for Center.
     *
     * @param locations - put locations.
     * @param center - put center.
     * @return new {@code Set<Location>}
     */
    Set<Location> updateCenterLocation(Set<LocationProfile> locations, Center center);

    /**
     *  The method returns updated {@code Set<Location>} for Club.
     *
     * @param locations - put locations.
     * @param club - put club.
     * @return
     */
    Set<Location> updateLocationByClub(Set<LocationResponse> locations, Club club);

    /**
     * The method returns entity {@code Location} of location by id.
     *
     * @param id - put location id.
     * @return new {@code Location}.
     * @throws NotExistException if location not exists.
     */
    Location getLocationById(Long id);

    /**
     * The method deletes entity {@code Location} and
     * returns dto {@code LocationResponse} of deleted location by id.
     *
     * @param id - id of location to delete
     * @return LocationResponse {@link  LocationResponse}.
     * @throws NotExistException {@link NotExistException} if the location doesn't exist.
     */
    LocationResponse deleteLocationById(Long id);
}
