package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.StudioResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedStudio;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Studio;

import java.util.List;

public interface StudioService {
    StudioResponse getStudioByProfileId(Long id);

    Studio getStudioById(Long id);
    Studio getStudioByName(String name);

    SuccessCreatedStudio addStudio(String name);

    List<StudioResponse> getListOfStudios();
}
