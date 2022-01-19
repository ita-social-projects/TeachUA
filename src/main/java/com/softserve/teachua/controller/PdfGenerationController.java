package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.service.CenterService;
import com.softserve.teachua.service.ClubService;
import com.softserve.teachua.service.documentreport.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfGenerationController implements Api {
    private final PdfGenerationService<ClubResponse> clubPdfGenerationService;
    private final PdfGenerationService<CenterResponse> centerPdfGenerationService;
    private final ClubService clubService;
    private final CenterService centerService;

    @Autowired
    public PdfGenerationController(PdfGenerationService<ClubResponse> clubPdfGenerationService,
                                   PdfGenerationService<CenterResponse> centerPdfGenerationService,
                                   ClubService clubService,
                                   CenterService centerService) {
        this.clubPdfGenerationService = clubPdfGenerationService;
        this.centerPdfGenerationService = centerPdfGenerationService;
        this.clubService = clubService;
        this.centerService = centerService;
    }

    @GetMapping(value = {"/pdf/club"}, produces = {"application/pdf"})
    public byte[] generateClubPdfReport(@RequestParam Long clubId) {
        return clubPdfGenerationService.getPdfOutput(clubService.getClubProfileById(clubId));
    }

    @GetMapping(value = {"/pdf/center"}, produces = {"application/pdf"})
    public byte[] generateCenterPdfReport(@RequestParam Long centerId) {
        return centerPdfGenerationService.getPdfOutput(centerService.getCenterProfileById(centerId));
    }
}