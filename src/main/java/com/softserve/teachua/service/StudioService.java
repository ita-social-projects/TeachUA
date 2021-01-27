package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.StudioResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedStudio;
import com.softserve.teachua.model.Center;

import java.util.List;

public interface StudioService {
    StudioResponse getStudioByProfileId(Long id);

    Center getStudioById(Long id);
    Center getStudioByName(String name);

    SuccessCreatedStudio addStudio(String name);

    List<StudioResponse> getListOfStudios();
}
