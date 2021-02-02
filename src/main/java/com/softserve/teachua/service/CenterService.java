package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.CenterResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCenter;
import com.softserve.teachua.dto.service.CenterProfile;
import com.softserve.teachua.model.Center;

import java.util.List;

public interface CenterService {
    CenterResponse getCenterByProfileId(Long id);

    Center getCenterById(Long id);

    Center getCenterByName(String name);

    SuccessCreatedCenter addCenter(CenterProfile centerProfile);

    List<CenterResponse> getListOfCenters();

    CenterProfile updateCenter(CenterProfile centerProfile);
}
