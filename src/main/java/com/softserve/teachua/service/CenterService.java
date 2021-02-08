package com.softserve.teachua.service;

import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.model.Center;

import java.util.List;

public interface CenterService {
    CenterResponse getCenterByProfileId(Long id);

    Center getCenterById(Long id);

    Center getCenterByName(String name);

    SuccessCreatedCenter addCenter(CenterProfile centerProfile);

    List<CenterResponse> getListOfCenters();

    CenterProfile updateCenter(Long id, CenterProfile centerProfile);

    CenterResponse deleteCenterById(Long id);
}
