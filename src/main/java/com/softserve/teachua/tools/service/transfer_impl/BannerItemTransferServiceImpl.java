package com.softserve.teachua.tools.service.transfer_impl;

import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.service.impl.BannerItemServiceImpl;
import com.softserve.teachua.tools.repository.BannerInfoRepository;
import com.softserve.teachua.tools.service.BannerItemTransferService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerItemTransferServiceImpl implements BannerItemTransferService {
    private final BannerItemServiceImpl bannerItemService;
    private final com.softserve.teachua.tools.FileUtils fileUtils;

    @Autowired
    public BannerItemTransferServiceImpl(BannerItemServiceImpl bannerItemService,
                                         com.softserve.teachua.tools.FileUtils fileUtils) {
        this.bannerItemService = bannerItemService;
        this.fileUtils = fileUtils;
    }

    @Override
    public List<SuccessCreatedBannerItem> moveBannerToDB() {
        return BannerInfoRepository.getBannersInfo().stream().map(banner -> {
            banner.setPicture(fileUtils.moveImage(banner.getPicture(), "banner"));
            return banner;
        }).map(bannerItemService::addBannerItem).toList();
    }
}
