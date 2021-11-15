package com.softserve.teachua.service;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import java.util.Set;

public interface LocationService {

    Location addLocation(LocationProfile locationProfile);
    Set<Location> updateCenterLocation(Set<LocationProfile> locations, Center center);
    Set<Location> updateLocationByClub(Set<LocationResponse> locations, Club club);

}
