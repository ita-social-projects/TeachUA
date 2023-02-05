package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.banner_item.BannerItemProfile;
import com.softserve.teachua.dto.banner_item.BannerItemResponse;
import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.service.BannerItemService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the banner items.
 */

@Slf4j
@RestController
@Tag(name = "banner", description = "the BannerItem API")
@SecurityRequirement(name = "api")
public class BannerItemController implements Api {
    private final BannerItemService bannerItemService;

    @Autowired
    public BannerItemController(BannerItemService bannerItemService) {
        this.bannerItemService = bannerItemService;
    }

    /**
     * The controller returns dto {@code BannerItemResponse} about banner item.
     *
     * @param id
     *            - put BannerItem id.
     *
     * @return new {@code BannerItemResponse}.
     */
    @GetMapping("/banner/{id}")
    public BannerItemResponse getBannerItem(@PathVariable Long id) {
        return bannerItemService.getBannerItemProfileById(id);
    }

    /**
     * The controller returns list of dto {@code List<BannerItemResponse>} of banner item.
     *
     * @return new {@code List<BannerItemResponse>}.
     */
    @GetMapping("/banners")
    public List<BannerItemResponse> getBunnerItems() {
        return bannerItemService.getListOfBannerItems();
    }

    /**
     * The controller returns dto {@code SuccessCreatedBannerItem} of created banner item.
     *
     * @param bannerItemProfile
     *            - place body to {@link BannerItemProfile}.
     *
     * @return new {@code SuccessCreatedBannerItem}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/banner")
    public SuccessCreatedBannerItem addBannerItem(@Valid @RequestBody BannerItemProfile bannerItemProfile) {
        return bannerItemService.addBannerItem(bannerItemProfile);
    }

    /**
     * The controller returns dto {@code BannerItemResponse } of updated banner item.
     *
     * @param id
     *            - put BannerItem id.
     * @param bannerItemProfile
     *            - place body to {@link BannerItemProfile}.
     *
     * @return new {@code BannerItemResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/banner/{id}")
    public BannerItemResponse updateBannerItem(@PathVariable Long id,
            @Valid @RequestBody BannerItemProfile bannerItemProfile) {
        return bannerItemService.updateBannerItem(id, bannerItemProfile);
    }

    /**
     * The controller returns dto {@code BannerItemResponse} of deleted banner item by id.
     *
     * @param id
     *            - put BannerItem id.
     *
     * @return new {@code BannerItemResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @DeleteMapping("/banner/{id}")
    public BannerItemResponse deleteBannerItem(@PathVariable Long id) {
        return bannerItemService.deleteBannerItemById(id);
    }
}
