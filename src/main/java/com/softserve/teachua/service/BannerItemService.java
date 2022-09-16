package com.softserve.teachua.service;

import com.softserve.teachua.dto.banner_item.*;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.BannerItem;

import java.util.List;

/**
 * This interface contains all needed methods to manage banner items using BannerItemRepository.
 */

public interface BannerItemService {
    /**
     * The method returns dto {@code BannerItemResponse} of banner item by id.
     *
     * @param id
     *            - put BannerItem id.
     *
     * @return new {@code BannerItemResponse}.
     *
     * @throws NotExistException
     *             if banner item not exists.
     */
    BannerItemResponse getBannerItemProfileById(Long id);

    /**
     * The method returns entity {@code BannerItem} of banner item by id.
     *
     * @param id
     *            - put BannerItem id.
     *
     * @return new {@code BannerItem}.
     *
     * @throws NotExistException
     *             if banner item not exists.
     */
    BannerItem getBannerItemById(Long id);

    /**
     * The method returns list of dto {@code List<BannerItemResponse>} of all banner items.
     *
     * @return new {@code List<BannerItemResponse>}.
     */
    List<BannerItemResponse> getListOfBannerItems();

    /**
     * The method returns dto {@code SuccessCreatedBannerItem} if banner item successfully added.
     *
     * @param bannerItemProfile
     *            - place body of dto {@code BannerItemProfile}.
     *
     * @return new {@code SuccessCreatedBannerItem}.
     */
    SuccessCreatedBannerItem addBannerItem(BannerItemProfile bannerItemProfile);

    /**
     * The method returns dto {@code BannerItemResponse} of updated banner item.
     *
     * @param id
     *            - put BannerItem id.
     * @param bannerItemProfile
     *            - place body of dto {@code BannerItemProfile}.
     *
     * @return new {@code BannerItemResponse}.
     */
    BannerItemResponse updateBannerItem(Long id, BannerItemProfile bannerItemProfile);

    /**
     * The method deletes entity {@code BannerItem} and returns dto {@code BannerItemResponse} of deleted banner item by
     * id.
     *
     * @param id
     *            - id of BannerItem to delete
     *
     * @return new {@code BannerItemResponse}.
     *
     * @throws NotExistException
     *             if banner item not exists.
     */
    BannerItemResponse deleteBannerItemById(Long id);
}
