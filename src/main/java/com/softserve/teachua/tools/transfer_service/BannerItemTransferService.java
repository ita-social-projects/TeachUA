package com.softserve.teachua.tools.transfer_service;

import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import java.util.List;

public interface BannerItemTransferService {
    public List<SuccessCreatedBannerItem> moveBannerToDB();
}
