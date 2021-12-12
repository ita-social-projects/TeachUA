package com.softserve.teachua.tools.transfer_service.transfer_impl;

import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.service.impl.BannerItemServiceImpl;
import com.softserve.teachua.tools.transfer_repository.MoveBannerRepository;
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
    private final MoveBannerRepository moveBannerRepository;

    @Autowired
    public BannerItemTransferServiceImpl(BannerItemServiceImpl bannerItemService, MoveBannerRepository moveBannerRepository) {
        this.bannerItemService = bannerItemService;
        this.moveBannerRepository = moveBannerRepository;
    }

    @Override
    public List<SuccessCreatedBannerItem> moveBannerToDB() {
        String pathToResources = "/src/main/resources/banner_image_for_db/";
        String pathToImages = "/target/upload/banner_Images";
        FileUtils.listFiles(new File(pathToResources), null, false)
                .forEach(file -> {
                    try {
                        FileUtils.moveToDirectory(new File(pathToResources + "/" + file.getName())
                                , new File(pathToImages),
                                true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return moveBannerRepository.getBanners().stream().map(bannerItemService::addBannerItem).collect(Collectors.toList());
    }
}
