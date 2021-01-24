package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.StudioResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedStudio;
import com.softserve.teachua.model.Studio;
import com.softserve.teachua.repository.StudioRepository;
import com.softserve.teachua.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudioServiceImpl implements StudioService {
    private final StudioRepository studioRepository;

    @Autowired
    public StudioServiceImpl(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    @Override
    public StudioResponse getStudioById(Long id) {
        Studio studio = studioRepository.getById(id);

        return StudioResponse.builder()
                .id(studio.getId())
                .name(studio.getName())
                .build();
    }

    @Override
    public SuccessCreatedStudio addStudio(String name) {
        Studio studio = studioRepository.save(Studio
                .builder()
                .name(name)
                .build());
        return SuccessCreatedStudio.builder()
                .id(studio.getId())
                .name(studio.getName())
                .build();
    }

    @Override
    public List<StudioResponse> getListOfStudios() {
        return studioRepository.findAll()
                .stream()
                .map(studio -> new StudioResponse(studio.getId(), studio.getName()))
                .collect(Collectors.toList());
    }
}
