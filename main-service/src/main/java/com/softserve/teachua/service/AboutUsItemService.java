package com.softserve.teachua.service;

import com.softserve.commons.util.marker.Archiver;
import com.softserve.teachua.dto.about_us_item.AboutUsItemProfile;
import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.model.AboutUsItem;
import java.util.List;

/**
 * This interface contains all needed methods to manage about us items using AboutUsItemRepository.
 */

public interface AboutUsItemService extends Archiver<Long> {
    /**
     * The method returns entity {@code AboutUsItem} of about us item by id.
     *
     * @param id
     *            - put AboutUsItem id.
     *
     * @return new {@code AboutUsItem}.
     *
     * @throws NotExistException
     *             if about us item not exists.
     */
    AboutUsItem getAboutUsItemById(Long id);

    /**
     * The method returns dto {@code AboutUsItemResponse} of about us item by id.
     *
     * @param id
     *            - put AboutUsItem id.
     *
     * @return new {@code AboutUsItemResponse}.
     *
     * @throws NotExistException
     *             if about us item not exists.
     */
    AboutUsItemResponse getAboutUsItemResponseById(Long id);

    /**
     * The method returns list of entities {@code List<AboutUsItem>} of all about us items.
     *
     * @return new {@code List<AboutUsItem>}.
     */
    List<AboutUsItem> getListOfAboutUsItems();

    /**
     * The method returns list of dto {@code List<AboutUsItemResponse>} of all about us items.
     *
     * @return new {@code List<AboutUsItemResponse>}.
     */
    List<AboutUsItemResponse> getListOfAboutUsItemResponses();

    /**
     * The method returns dto {@code AboutUsItemResponse} if about us item successfully added.
     *
     * @param aboutUsItemProfile
     *            - place body of dto {@code AboutUsItemProfile}.
     *
     * @return new {@code AboutUsItemResponse}.
     */
    AboutUsItemResponse addAboutUsItem(AboutUsItemProfile aboutUsItemProfile);

    /**
     * The method returns dto {@code AboutUsItemResponse} of updated about us item.
     *
     * @param id
     *            - put AboutUsItem id.
     * @param aboutUsItemProfile
     *            - place body of dto {@code AboutUsItemProfile}.
     *
     * @return new {@code AboutUsItemResponse}.
     */
    AboutUsItemResponse updateAboutUsItem(Long id, AboutUsItemProfile aboutUsItemProfile);

    /**
     * The method deletes entity {@code AboutUsItem} returns dto {@code AboutUsItemResponse} of deleted about us item by
     * id.
     *
     * @param id
     *            - id of AboutUsItem to delete.
     *
     * @return new {@code AboutUsItemResponse}.
     *
     * @throws NotExistException
     *             if about us item not exists.
     */
    AboutUsItemResponse deleteAboutUsItemById(Long id);

    /**
     * The method validates video url and returns valid link.
     *
     * @param aboutUsItemProfile
     *            - place body of dto {@code AboutUsItemProfile}.
     *
     * @return valid url.
     *
     * @throws IllegalArgumentException
     *             if url is invalid.
     */
    String validateVideoUrl(AboutUsItemProfile aboutUsItemProfile);

    /**
     * The method changes an order of about us items.
     */
    void changeOrder(Long id, Long position);
}
