package com.softserve.teachua.tools.transfer_service.transfer_impl;

import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.service.impl.BannerItemServiceImpl;
import com.softserve.teachua.tools.info_repository.BannerInfoRepository;
import com.softserve.teachua.tools.transfer_service.BannerItemTransferService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerItemTransferServiceImpl implements BannerItemTransferService {

    private final BannerItemServiceImpl bannerItemService;
    private final com.softserve.teachua.tools.FileUtils fileUtils;

    @Autowired
    public BannerItemTransferServiceImpl(BannerItemServiceImpl bannerItemService, com.softserve.teachua.tools.FileUtils fileUtils) {
        this.bannerItemService = bannerItemService;
        this.fileUtils = fileUtils;
    }

    @Override
    public List<SuccessCreatedBannerItem> moveBannerToDB() {

        BannerInfoRepository.getBannersInfo().forEach(banner -> fileUtils.moveImage(banner.getPicture(),"banner"));
        return BannerInfoRepository.getBannersInfo().stream().map(bannerItemService::addBannerItem).collect(Collectors.toList());
    }
}
