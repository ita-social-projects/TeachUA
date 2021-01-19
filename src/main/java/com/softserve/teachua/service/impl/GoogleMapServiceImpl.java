package com.softserve.teachua.service.impl;

import com.softserve.teachua.entity.Coordinates;
import com.softserve.teachua.repository.GoogleMapRepository;
import com.softserve.teachua.service.GoogleMapService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleMapServiceImpl implements GoogleMapService {
    @Autowired
    private GoogleMapRepository googleMapRepository;

    @Override
    public Coordinates getClubCoordinatesByAddress(String centerAddress) {
        return googleMapRepository.getClubCoordinatesByAddress(centerAddress);
    }
}
