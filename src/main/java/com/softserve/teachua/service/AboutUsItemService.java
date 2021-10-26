package com.softserve.teachua.service;

import com.softserve.teachua.dto.about_us_item.AboutUsItemProfile;
import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.teachua.model.AboutUsItem;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AboutUsItemService {

    AboutUsItem getAboutUsItemById(Long id);

    AboutUsItemResponse getAboutUsItemResponseById(Long id);

    List<AboutUsItem> getListOfAboutUsItems();

    List<AboutUsItemResponse> getListOfAboutUsItemResponses();

    AboutUsItemResponse addAboutUsItem(AboutUsItemProfile aboutUsItemProfile);

    AboutUsItemResponse updateAboutUsItem(Long id, AboutUsItemProfile aboutUsItemProfile);

    AboutUsItemResponse deleteAboutUsItemById(Long id);

    void validateVideoUrl(AboutUsItemProfile aboutUsItemProfile);
}
