package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.about_us_item.AboutUsItemProfile;
import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.AboutUsItem;
import com.softserve.teachua.repository.AboutUsItemRepository;
import com.softserve.teachua.service.AboutUsItemService;
import com.softserve.teachua.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AboutUsItemServiceImpl implements AboutUsItemService {
    private static final String ABOUT_US_ITEM_NOT_FOUND_BY_ID = "About us item was not found by id: %s";
    private static final String ABOUT_US_ITEM_NULL_FIELD_ERROR = "About us item cannot exist without \"%s\"";

    private final AboutUsItemRepository aboutUsItemRepository;
    private final ArchiveService archiveService;
    private final DtoConverter dtoConverter;

    @Autowired
    public AboutUsItemServiceImpl(
            AboutUsItemRepository aboutUsItemRepository,
            ArchiveService archiveService,
            DtoConverter dtoConverter
    ) {
        this.aboutUsItemRepository = aboutUsItemRepository;
        this.archiveService = archiveService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public AboutUsItem getAboutUsItemById(Long id) {
        return aboutUsItemRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(ABOUT_US_ITEM_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public AboutUsItemResponse getAboutUsItemResponseById(Long id) {
        return dtoConverter.convertToDto(getAboutUsItemById(id), AboutUsItemResponse.class);
    }

    @Override
    public List<AboutUsItem> getListOfAboutUsItems() {
        return aboutUsItemRepository.findAll();
    }

    @Override
    public List<AboutUsItemResponse> getListOfAboutUsItemResponses() {
        List<AboutUsItemResponse> aboutUsItemResponses = aboutUsItemRepository.findAll()
                .stream()
                .map(item -> (AboutUsItemResponse)dtoConverter.convertToDto(item, AboutUsItemResponse.class))
                .collect(Collectors.toList());
        return aboutUsItemResponses;
    }

    @Override
    public AboutUsItemResponse addAboutUsItem(AboutUsItemProfile aboutUsItemProfile) {
        AboutUsItem aboutUsItem = dtoConverter.convertToEntity(aboutUsItemProfile, new AboutUsItem());
        return dtoConverter.convertToDto(aboutUsItemRepository.save(aboutUsItem), AboutUsItemResponse.class);
    }

    @Override
    public AboutUsItemResponse updateAboutUsItem(Long id, AboutUsItemProfile aboutUsItemProfile) {
        AboutUsItem aboutUsItem = getAboutUsItemById(id);
        BeanUtils.copyProperties(aboutUsItemProfile, aboutUsItem);
        return dtoConverter.convertToDto(aboutUsItemRepository.save(aboutUsItem), AboutUsItemResponse.class);
    }

    @Override
    public AboutUsItemResponse deleteAboutUsItemById(Long id) {
        AboutUsItem aboutUsItem = getAboutUsItemById(id);
        aboutUsItemRepository.deleteById(id);
        aboutUsItemRepository.flush();
        return dtoConverter.convertToDto(aboutUsItem, AboutUsItemResponse.class);
    }

}
