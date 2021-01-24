package com.softserve.teachua.service;

import com.softserve.teachua.dto.controller.StudioResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedStudio;

import java.util.List;

public interface StudioService {

    StudioResponse getStudioById(Long id);

    SuccessCreatedStudio addStudio(String name);

    List<StudioResponse> getListOfStudios();
}
