package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.service.ClubService;
import com.softserve.teachua.service.FeedbackService;
import com.softserve.teachua.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfGenerationController implements Api {
    PdfGenerationService<ClubResponse> pdfGenerationService;
    ClubService clubService;

    @Autowired
    public PdfGenerationController(PdfGenerationService<ClubResponse> pdfGenerationService, ClubService clubService) {
        this.pdfGenerationService = pdfGenerationService;
        this.clubService = clubService;
    }

    @GetMapping(value = {"/pdf/club"}, produces = {"application/pdf"})
    public byte[] generateFeedbackPdf(@RequestParam Long id) {
        return this.pdfGenerationService.getPdfOutput(this.clubService.getClubProfileById(id));
    }
}