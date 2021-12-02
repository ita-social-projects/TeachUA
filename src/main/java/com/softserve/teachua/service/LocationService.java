package com.softserve.teachua.service;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.location.LocationResponse;
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

    Location getLocationById(Long id);

    LocationResponse deleteLocationById(Long id);
}
