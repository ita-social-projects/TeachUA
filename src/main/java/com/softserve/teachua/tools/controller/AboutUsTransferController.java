package com.softserve.teachua.tools.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.about_us_item.AboutUsItemResponse;
import com.softserve.teachua.tools.service.AboutUsTransferService;
import com.softserve.teachua.utils.annotation.DevPermit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AboutUsTransferController implements Api {
    private final AboutUsTransferService aboutUsTransferService;

    @Autowired
    public AboutUsTransferController(AboutUsTransferService aboutUsTransferService) {
        this.aboutUsTransferService = aboutUsTransferService;
    }

    @DevPermit
    @PostMapping("/transferAboutUsToDB")
    public List<AboutUsItemResponse> moveAboutUsToDB() {
        return aboutUsTransferService.moveAboutUsToDB();
    }
}
