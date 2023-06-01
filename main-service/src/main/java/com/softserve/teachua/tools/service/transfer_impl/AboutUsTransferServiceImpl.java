package com.softserve.teachua.tools.service.transfer_impl;

import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.teachua.service.AboutUsItemService;
import com.softserve.teachua.tools.FileUtils;
import com.softserve.teachua.tools.repository.AboutUsInfoRepository;
import com.softserve.teachua.tools.service.AboutUsTransferService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AboutUsTransferServiceImpl implements AboutUsTransferService {
    private final AboutUsItemService aboutUsItemService;
    private final FileUtils fileUtils;

    @Autowired
    public AboutUsTransferServiceImpl(AboutUsItemService aboutUsItemService,
                                      FileUtils fileUtils) {
        this.aboutUsItemService = aboutUsItemService;
        this.fileUtils = fileUtils;
    }

    @Override
    public List<AboutUsItemResponse> moveAboutUsToDB() {
        return AboutUsInfoRepository.getAboutUsInfo().stream().map(aboutUs -> {
            log.info(aboutUs.toString());
            if (aboutUs.getType().equals(3L) || aboutUs.getType().equals(4L)) {
                aboutUs.setPicture(fileUtils.moveImage(aboutUs.getPicture(), "aboutUs"));
            }
            return aboutUs;
        }).map(aboutUsItemService::addAboutUsItem).toList();
    }
}
