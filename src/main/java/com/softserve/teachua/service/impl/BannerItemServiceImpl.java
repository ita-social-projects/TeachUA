package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.archive.ArchiveProfile;
import com.softserve.teachua.dto.banner_item.BannerItemProfile;
import com.softserve.teachua.dto.banner_item.BannerItemResponse;
import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.BannerItem;
import com.softserve.teachua.repository.BannerItemRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.BannerItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BannerItemServiceImpl implements BannerItemService, ArchiveMark<BannerItem> {
    private static final String BANNER_ITEM_NOT_FOUND_BY_ID = "Banner Item not found by id: %s";
    private static final String BANNER_ITEM_DELETING_ERROR = "Banner Item can`t be deleted by id: %s";

    private final BannerItemRepository bannerItemRepository;
    private final ArchiveService archiveService;
    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;

    @Autowired
    public BannerItemServiceImpl(
            BannerItemRepository bannerItemRepository,
            ArchiveService archiveService,
            DtoConverter dtoConverter, ObjectMapper objectMapper) {
        this.bannerItemRepository = bannerItemRepository;
        this.archiveService = archiveService;
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
    }

    @Override
    public BannerItemResponse getBannerItemProfileById(Long id) {
        return dtoConverter.convertToDto(getBannerItemById(id), BannerItemResponse.class);
    }

    @Override
    public BannerItem getBannerItemById(Long id) {
        return bannerItemRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(BANNER_ITEM_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public List<BannerItemResponse> getListOfBannerItems() {
        List<BannerItemResponse> itemResponses = bannerItemRepository.findAllByOrderBySequenceNumberAsc()
                .stream()
                .map(item -> (BannerItemResponse) dtoConverter.convertToDto(item, BannerItemResponse.class))
                .collect(Collectors.toList());

        log.debug("**/getting list of cities = " + itemResponses);
        return itemResponses;
    }

    @Override
    public SuccessCreatedBannerItem addBannerItem(BannerItemProfile bannerItemProfile) {
        BannerItem bannerItem =
                bannerItemRepository.save(dtoConverter.convertToEntity(bannerItemProfile, new BannerItem()));

        log.debug("**/adding new banner item = " + bannerItem);
        return dtoConverter.convertToDto(bannerItem, SuccessCreatedBannerItem.class);
    }

    @Override
    public BannerItemResponse updateBannerItem(Long id, BannerItemProfile bannerItemProfile) {
        BannerItem bannerItem = getBannerItemById(id);
        BannerItem newBannerItem = dtoConverter.convertToEntity(bannerItemProfile, bannerItem)
                .withId(id);

        log.info("**/updating banner item by id = " + newBannerItem);
        return dtoConverter.convertToDto(bannerItemRepository.save(newBannerItem), BannerItemResponse.class);
    }

    @Override
    public BannerItemResponse deleteBannerItemById(Long id) {
        BannerItem bannerItem = getBannerItemById(id);

        archiveModel(bannerItem);

        try {
            bannerItemRepository.deleteById(id);
            bannerItemRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(String.format(BANNER_ITEM_DELETING_ERROR, id));
        }

        log.info("banner item {} was successfully deleted", bannerItem);
        return dtoConverter.convertToDto(bannerItem, BannerItemResponse.class);
    }

    @Override
    public void archiveModel(BannerItem bannerItem) {
        BannerItemProfile bannerItemProfile = dtoConverter.convertToDto(bannerItem, BannerItemProfile.class);
        archiveService.saveModel(ArchiveProfile.builder()
                .serviceClassName(getClass().getSimpleName())
                .data(bannerItemProfile)
                .build());
    }

    @Override
    public void restoreModel(String archiveObject) {
        try {
            BannerItemProfile bannerItemProfile = objectMapper.readValue(archiveObject, BannerItemProfile.class);
            addBannerItem(bannerItemProfile);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
