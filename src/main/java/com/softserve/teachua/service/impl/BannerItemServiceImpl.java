package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.banner_item.*;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.BannerItem;
import com.softserve.teachua.repository.BannerItemRepository;
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
public class BannerItemServiceImpl implements BannerItemService {
    private final String BANNER_ITEM_NOT_FOUND_BY_ID = "Banner Item not found by id: %s";
    private final String BANNER_ITEM_DELETING_ERROR = "Banner Item can`t be deleted by id: %s";

    private final BannerItemRepository bannerItemRepository;
    private final ArchiveService archiveService;
    private final DtoConverter dtoConverter;

    @Autowired
    public BannerItemServiceImpl(
            BannerItemRepository bannerItemRepository,
            ArchiveService archiveService,
            DtoConverter dtoConverter) {
        this.bannerItemRepository = bannerItemRepository;
        this.archiveService = archiveService;
        this.dtoConverter = dtoConverter;
    }

    /**
     * The method returns dto {@code BannerItemResponse} of banner item by id.
     *
     * @param id - put BannerItem id.
     * @return new {@code BannerItemResponse}.
     */
    @Override
    public BannerItemResponse getBannerItemProfileById(Long id) {
        return dtoConverter.convertToDto(getBannerItemById(id), BannerItemResponse.class);
    }

    /**
     * The method returns entity {@code BannerItem} of banner item by id.
     *
     * @param id - put BannerItem id.
     * @return new {@code BannerItem}.
     * @throws NotExistException if banner item not exists.
     */
    @Override
    public BannerItem getBannerItemById(Long id) {
        return bannerItemRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(BANNER_ITEM_NOT_FOUND_BY_ID, id)));
    }

    /**
     * The method returns list of dto {@code List<BannerItemResponse>} of all banner items.
     *
     * @return new {@code List<BannerItemResponse>}.
     */
    @Override
    public List<BannerItemResponse> getListOfBannerItems() {
        List<BannerItemResponse> itemResponses = bannerItemRepository.findAllByOrderBySequenceNumberAsc()
                .stream()
                .map(item -> (BannerItemResponse) dtoConverter.convertToDto(item, BannerItemResponse.class))
                .collect(Collectors.toList());

        log.info("**/getting list of cities = " + itemResponses);
        return itemResponses;
    }

    /**
     * The method returns dto {@code SuccessCreatedBannerItem} if banner item successfully added.
     *
     * @param bannerItemProfile - place body of dto {@code BannerItemProfile}.
     * @return new {@code SuccessCreatedBannerItem}.
     */
    @Override
    public SuccessCreatedBannerItem addBannerItem(BannerItemProfile bannerItemProfile) {
        BannerItem bannerItem =
                bannerItemRepository.save(dtoConverter.convertToEntity(bannerItemProfile, new BannerItem()));

        log.info("**/adding new banner item = " + bannerItem);
        return dtoConverter.convertToDto(bannerItem, SuccessCreatedBannerItem.class);
    }

    /**
     * The method returns dto {@code BannerItemResponse} of updated banner item.
     *
     * @param id - put BannerItem id.
     * @param bannerItemProfile - place body of dto {@code BannerItemProfile}.
     * @return new {@code BannerItemResponse}.
     */
    @Override
    public BannerItemResponse updateBannerItem(Long id, BannerItemProfile bannerItemProfile) {
        BannerItem bannerItem = getBannerItemById(id);
        BannerItem newBannerItem = dtoConverter.convertToEntity(bannerItemProfile, bannerItem)
                .withId(id);

        log.info("**/updating banner item by id = " + newBannerItem);
        return dtoConverter.convertToDto(bannerItemRepository.save(newBannerItem), BannerItemResponse.class);
    }

    /**
     * The method deletes entity {@code BannerItem} and
     * returns dto {@code BannerItemResponse} of deleted banner item by id
     *
     * @param id - id of BannerItem to delete
     * @return new {@code BannerItemResponse}.
     * @throws NotExistException if banner item not exists.
     */
    @Override
    public BannerItemResponse deleteBannerItemById(Long id) {
        BannerItem bannerItem = getBannerItemById(id);

        archiveService.saveModel(bannerItem);

        try {
            bannerItemRepository.deleteById(id);
            bannerItemRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(String.format(BANNER_ITEM_DELETING_ERROR, id));
        }

        log.info("banner item {} was successfully deleted", bannerItem);
        return dtoConverter.convertToDto(bannerItem, BannerItemResponse.class);
    }
}
