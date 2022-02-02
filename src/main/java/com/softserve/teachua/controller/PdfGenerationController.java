package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.service.CenterService;
import com.softserve.teachua.service.ClubService;
import com.softserve.teachua.service.documentreport.ReportGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfGenerationController implements Api {
    private final ReportGenerationService<ClubResponse> clubReportGenerationService;
    private final ReportGenerationService<CenterResponse> centerReportGenerationService;
    private final ReportGenerationService<Page<ClubResponse>> resultSearchReportGenerationService;
    private final ClubService clubService;
    private final CenterService centerService;

    @Autowired
    public PdfGenerationController(ReportGenerationService<ClubResponse> clubReportGenerationService,
                                   ReportGenerationService<CenterResponse> centerReportGenerationService,
                                   ReportGenerationService<Page<ClubResponse>> resultSearchReportGenerationService,
                                   ClubService clubService,
                                   CenterService centerService) {
        this.clubReportGenerationService = clubReportGenerationService;
        this.centerReportGenerationService = centerReportGenerationService;
        this.resultSearchReportGenerationService = resultSearchReportGenerationService;
        this.clubService = clubService;
        this.centerService = centerService;
    }

    @GetMapping(value = {"/pdf/club/{clubId}"}, produces = {"application/pdf"})
    public byte[] generateClubPdfReport(@PathVariable Long clubId) {
        return clubReportGenerationService.getPdfOutput(clubService.getClubProfileById(clubId));
    }

    @GetMapping(value = {"/pdf/center/{centerId}"}, produces = {"application/pdf"})
    public byte[] generateCenterPdfReport(@PathVariable Long centerId) {
        return centerReportGenerationService.getPdfOutput(centerService.getCenterProfileById(centerId));
    }

    @GetMapping(value = {"/pdf/resultsearch"}, produces = {"application/pdf"})
    public byte[] generateResultSearchPdfReport(SearchClubProfile searchClubProfile,
                                                @PageableDefault(sort = "id", value = 50) Pageable pageable) {
        return resultSearchReportGenerationService.getPdfOutput(clubService.getClubsBySearchParameters(searchClubProfile, pageable));
    }
}