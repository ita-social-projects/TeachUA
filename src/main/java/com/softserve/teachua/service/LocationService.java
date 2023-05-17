package com.softserve.teachua.service;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import java.util.List;
import java.util.Set;

/**
 * This interface contains all needed methods to manage locations.
 */

public interface LocationService {
    /**
     * The method returns {@code Location} entity.
     *
     * @param locationProfile
     *            - place body of dto {@code LocationProfile}.
     *
     * @return new {@code Location}.
     */
    Location addLocation(LocationProfile locationProfile);

    /**
     * The method returns entity {@code Location} of updated location.
     *
     * @param id
     *            - put Location id.
     * @param locationProfile
     *            - place body of dto {@code LocationProfile}.
     *
     * @return new {@code Location}.
     */
    Location updateLocation(Long id, LocationProfile locationProfile);

    /**
     * The method returns updated {@code Set<Location>} for Center.
     *
     * @param locations
     *            - put locations.
     * @param center
     *            - put center.
     *
     * @return new {@code Set<Location>}
     */
    Set<Location> updateCenterLocation(Set<LocationProfile> locations, Center center);

    /**
     * The method returns updated {@code Set<Location>} for Club.
     *
     * @param locations
     *            - put locations.
     * @param club
     *            - put club.
     *
     * @return a set of locations
     */
    Set<Location> updateLocationByClub(Set<LocationProfile> locations, Club club);

    /**
     * The method returns entity {@code Location} of location by id.
     *
     * @param id
     *            - put location id.
     *
     * @return new {@code Location}.
     *
     * @throws NotExistException
     *             if location not exists.
     */
    Location getLocationById(Long id);

    /**
     * The method returns list of entities {@code List<Location>}.
     *
     * @return new {@code List<Location>}.
     */
    List<Location> getListOfAllLocations();

    /**
     * The method deletes entity {@code Location} and returns dto {@code LocationProfile} of deleted location by id.
     *
     * @param id
     *            - id of location to delete
     *
     * @return LocationProfile {@link LocationProfile}.
     *
     * @throws NotExistException
     *             {@link NotExistException} if the location doesn't exist.
     */
    LocationProfile deleteLocationById(Long id);
}
