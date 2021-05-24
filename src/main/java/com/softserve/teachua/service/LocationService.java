package com.softserve.teachua.service;

import com.softserve.teachua.dto.databaseTransfer.model.LocationExcel;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.model.Location;

public interface LocationService {

    Location addLocation(LocationProfile locationProfile);

}
