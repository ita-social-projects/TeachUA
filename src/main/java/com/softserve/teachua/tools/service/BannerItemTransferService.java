package com.softserve.teachua.tools.service;

import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import java.util.List;

public interface BannerItemTransferService {
    /**
     * The method for add hardCoded Banner to DB
     *
     * @return new {@code List<SuccessCreatedBannerItem>}
     */
    public List<SuccessCreatedBannerItem> moveBannerToDB();
}
