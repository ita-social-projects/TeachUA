package com.softserve.teachua.service;

import com.softserve.teachua.dto.banner_item.*;
import com.softserve.teachua.model.BannerItem;

import java.util.List;

public interface BannerItemService {

    BannerItemResponse getBannerItemProfileById(Long id);

    BannerItem getBannerItemById(Long id);

    List<BannerItemResponse> getListOfBannerItems();

    SuccessCreatedBannerItem addBannerItem(BannerItemProfile bannerItemProfile);

    BannerItemResponse updateBannerItem(Long id, BannerItemProfile bannerItemProfile);

    BannerItemResponse deleteBannerItemById(Long id);
}
