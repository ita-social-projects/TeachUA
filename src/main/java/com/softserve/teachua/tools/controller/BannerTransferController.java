package com.softserve.teachua.tools.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.tools.service.BannerItemTransferService;
import com.softserve.teachua.tools.service.transfer_impl.BannerItemTransferServiceImpl;
import com.softserve.teachua.utils.annotation.DevPermit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BannerTransferController implements Api {
    private final BannerItemTransferService bannerItemTransferService;

    @Autowired
    public BannerTransferController(BannerItemTransferServiceImpl bannerItemTransferService) {
        this.bannerItemTransferService = bannerItemTransferService;
    }

    /**
     * Use this controller for add hardCoded Banner to DB on prod\dev
     *
     * @return new {@code List<SuccessCreatedBannerItem}
     */
    @DevPermit
    @PostMapping("/transferBannersToDB")
    public List<SuccessCreatedBannerItem> moveBannerToDB() {
        return bannerItemTransferService.moveBannerToDB();
    }
}
