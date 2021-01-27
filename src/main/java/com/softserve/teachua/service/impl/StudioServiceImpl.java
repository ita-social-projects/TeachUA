package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.StudioResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedStudio;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.repository.CenterRepository;
import com.softserve.teachua.service.StudioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudioServiceImpl implements StudioService {
    private static final String STUDIO_ALREADY_EXIST = "Center already exist with name: %s";
    private static final String STUDIO_NOT_FOUND_BY_ID = "Center not found by id: %s";
    private static final String STUDIO_NOT_FOUND_BY_NAME = "Center not found by name: %s";

    private final CenterRepository centerRepository;

    @Autowired
    public StudioServiceImpl(CenterRepository centerRepository) {
        this.centerRepository = centerRepository;
    }

    @Override
    public StudioResponse getStudioByProfileId(Long id) {
        Center center = getStudioById(id);
        return StudioResponse.builder()
                .id(center.getId())
                .name(center.getName())
                .build();
    }

    @Override
    public Center getStudioById(Long id) {
        if (!isStudioExistById(id)) {
            String studioNotFoundById = String.format(STUDIO_NOT_FOUND_BY_ID, id);
            log.error(studioNotFoundById);
            throw new NotExistException(studioNotFoundById);
        }

        Center center = centerRepository.getById(id);
        log.info("**/getting center by id = " + center);
        return center;
    }

    @Override
    public Center getStudioByName(String name) {
        if (!isStudioExistByName(name)) {
            String studioNotFoundById = String.format(STUDIO_NOT_FOUND_BY_NAME, name);
            log.error(studioNotFoundById);
            throw new NotExistException(studioNotFoundById);
        }

        Center center = centerRepository.findByName(name);
        log.info("**/getting center by name = " + name);
        return center;
    }

    @Override
    public SuccessCreatedStudio addStudio(String name) {
        if (isStudioExistByName(name)) {
            String studioAlreadyExist = String.format(STUDIO_ALREADY_EXIST, name);
            log.error(studioAlreadyExist);
            throw new AlreadyExistException(studioAlreadyExist);
        }

        Center center = centerRepository.save(Center
                .builder()
                .name(name)
                .build());

        log.info("**/adding new center = " + center);
        return SuccessCreatedStudio.builder()
                .id(center.getId())
                .name(center.getName())
                .build();
    }

    @Override
    public List<StudioResponse> getListOfStudios() {
        List<StudioResponse> studioResponses = centerRepository.findAll()
                .stream()
                .map(studio -> new StudioResponse(studio.getId(), studio.getName()))
                .collect(Collectors.toList());

        log.info("**/getting list of studios = " + studioResponses);
        return studioResponses;
    }

    private boolean isStudioExistById(Long id) {
        return centerRepository.existsById(id);
    }

    private boolean isStudioExistByName(String name) {
        return centerRepository.existsByName(name);
    }
}
