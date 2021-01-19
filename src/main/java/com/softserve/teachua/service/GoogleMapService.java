package com.softserve.teachua.service;

import com.softserve.teachua.entity.Coordinates;
import java.util.List;

public interface GoogleMapService {
    Coordinates getClubCoordinatesByAddress(String centerAddress);
}
