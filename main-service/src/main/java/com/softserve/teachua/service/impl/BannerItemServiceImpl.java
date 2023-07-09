package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import com.softserve.teachua.dto.banner_item.BannerItemProfile;
import com.softserve.teachua.dto.banner_item.BannerItemResponse;
import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.model.BannerItem;
import com.softserve.teachua.repository.BannerItemRepository;
import com.softserve.teachua.service.BannerItemService;
import jakarta.validation.ValidationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class BannerItemServiceImpl implements BannerItemService {
    private static final String BANNER_ITEM_NOT_FOUND_BY_ID = "Banner Item not found by id: %s";
    private static final String BANNER_ITEM_DELETING_ERROR = "Banner Item can`t be deleted by id: %s";

    private final BannerItemRepository bannerItemRepository;
    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;
    private final ArchiveMQMessageProducer<BannerItem> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;

    public BannerItemServiceImpl(BannerItemRepository bannerItemRepository, DtoConverter dtoConverter,
                                 ObjectMapper objectMapper,
                                 ArchiveMQMessageProducer<BannerItem> archiveMQMessageProducer,
                                 ArchiveClient archiveClient) {
        this.bannerItemRepository = bannerItemRepository;
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
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

    private void archiveModel(BannerItem bannerItem) {
        archiveMQMessageProducer.publish(bannerItem);
    }

    @Override
    public void restoreModel(Long id) {
        var aboutUsItem = objectMapper.convertValue(
                archiveClient.restoreModel(BannerItem.class.getName(), id),
                BannerItem.class);

        bannerItemRepository.save(aboutUsItem);
    }


}
