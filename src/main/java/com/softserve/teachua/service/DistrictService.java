package com.softserve.teachua.service;

import com.softserve.teachua.dto.district.DistrictProfile;
import com.softserve.teachua.dto.district.DistrictResponse;
import com.softserve.teachua.dto.district.SuccessCreatedDistrict;
import com.softserve.teachua.model.District;

import java.util.List;
import java.util.Optional;

public interface DistrictService {
    DistrictResponse getDistrictProfileById(Long id);

    District getDistrictById(Long id);

    District getDistrictByName(String name);

    Optional<District> getOptionalDistrictByName(String name);

    SuccessCreatedDistrict addDistrict(DistrictProfile districtProfile);

    List<DistrictResponse> getListOfDistricts();

    List<DistrictResponse> getListOfDistrictsByCityName(String name);

    DistrictProfile updateDistrict(Long id, DistrictProfile districtProfile);

    DistrictResponse deleteDistrictById(Long id);
}
