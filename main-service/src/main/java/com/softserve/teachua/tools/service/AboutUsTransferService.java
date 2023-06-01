package com.softserve.teachua.tools.service;

import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import java.util.List;

public interface AboutUsTransferService {
    List<AboutUsItemResponse> moveAboutUsToDB();
}
