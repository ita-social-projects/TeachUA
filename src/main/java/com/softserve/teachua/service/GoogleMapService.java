package com.softserve.teachua.service;

import com.softserve.teachua.entity.Coordinates;

public interface GoogleMapService {
    Coordinates getClubCoordinatesByAddress(String centerAddress);
}
