package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.banner_item.BannerItemProfile;
import com.softserve.teachua.dto.banner_item.BannerItemResponse;
import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.clients.exception.NotExistException;
import com.softserve.teachua.model.BannerItem;
import com.softserve.teachua.model.archivable.BannerItemArch;
import com.softserve.teachua.repository.BannerItemRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.BannerItemService;
import java.util.List;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public BannerItemServiceImpl(BannerItemRepository bannerItemRepository, ArchiveService archiveService,
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
        List<BannerItemResponse> itemResponses = bannerItemRepository.findAllByOrderBySequenceNumberAsc().stream()
                .map(item -> (BannerItemResponse) dtoConverter.convertToDto(item, BannerItemResponse.class))
                .toList();

        log.debug("**/getting list of cities = " + itemResponses);
        return itemResponses;
    }

    @Override
    public SuccessCreatedBannerItem addBannerItem(BannerItemProfile bannerItemProfile) {
        BannerItem bannerItem = bannerItemRepository
                .save(dtoConverter.convertToEntity(bannerItemProfile, new BannerItem()));

        log.debug("**/adding new banner item = " + bannerItem);
        return dtoConverter.convertToDto(bannerItem, SuccessCreatedBannerItem.class);
    }

    @Override
    public BannerItemResponse updateBannerItem(Long id, BannerItemProfile bannerItemProfile) {
        BannerItem bannerItem = getBannerItemById(id);
        BannerItem newBannerItem = dtoConverter.convertToEntity(bannerItemProfile, bannerItem).withId(id);

        log.info("**/updating banner item by id = " + newBannerItem);
        return dtoConverter.convertToDto(bannerItemRepository.save(newBannerItem), BannerItemResponse.class);
    }

    @Override
    public BannerItemResponse deleteBannerItemById(Long id) {
        BannerItem bannerItem = getBannerItemById(id);

        try {
            bannerItemRepository.deleteById(id);
            bannerItemRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(String.format(BANNER_ITEM_DELETING_ERROR, id));
        }

        archiveModel(bannerItem);

        log.info("banner item {} was successfully deleted", bannerItem);
        return dtoConverter.convertToDto(bannerItem, BannerItemResponse.class);
    }

    @Override
    public void archiveModel(BannerItem bannerItem) {
        archiveService.saveModel(dtoConverter.convertToDto(bannerItem, BannerItemArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        BannerItemArch bannerItemArh = objectMapper.readValue(archiveObject, BannerItemArch.class);
        BannerItem bannerItem = dtoConverter.convertToEntity(bannerItemArh, BannerItem.builder().build());
        bannerItemRepository.save(bannerItem);
    }
}
