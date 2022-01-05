package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.service.FeedbackService;
import com.softserve.teachua.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfGenerationController implements Api {
    PdfGenerationService<FeedbackResponse> pdfGenerationService;
    FeedbackService feedbackService;

    @Autowired
    public PdfGenerationController(PdfGenerationService<FeedbackResponse> pdfGenerationService, FeedbackService feedbackService) {
        this.pdfGenerationService = pdfGenerationService;
        this.feedbackService = feedbackService;
    }

    @GetMapping(value = {"/pdf"}, produces = {"application/pdf"})
    public byte[] generateFeedbackPdf(@RequestParam Long id) {
        return this.pdfGenerationService.getPdfOutput(this.feedbackService.getFeedbackProfileById(id));
    }
}