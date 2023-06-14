package com.softserve.club.controller;

import com.softserve.club.controller.marker.Api;
import com.softserve.club.dto.center.CenterResponse;
import com.softserve.club.dto.club.ClubResponse;
import com.softserve.club.dto.search.SearchClubProfile;
import com.softserve.club.service.CenterService;
import com.softserve.club.service.ClubService;
import com.softserve.club.util.documentreport.ReportGenerationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PdfGenerationController implements Api {
    private final ReportGenerationService<ClubResponse> clubReportGenerationService;
    private final ReportGenerationService<CenterResponse> centerReportGenerationService;
    private final ReportGenerationService<Page<ClubResponse>> resultSearchReportGenerationService;
    private final ClubService clubService;
    private final CenterService centerService;

    public PdfGenerationController(ReportGenerationService<ClubResponse> clubReportGenerationService,
            ReportGenerationService<CenterResponse> centerReportGenerationService,
            ReportGenerationService<Page<ClubResponse>> resultSearchReportGenerationService, ClubService clubService,
            CenterService centerService) {
        this.clubReportGenerationService = clubReportGenerationService;
        this.centerReportGenerationService = centerReportGenerationService;
        this.resultSearchReportGenerationService = resultSearchReportGenerationService;
        this.clubService = clubService;
        this.centerService = centerService;
    }

    @GetMapping(value = { "/club/pdf/{clubId}" }, produces = { "application/pdf" })
    public byte[] generateClubPdfReport(@PathVariable Long clubId) {
        return clubReportGenerationService.getPdfOutput(clubService.getClubProfileById(clubId));
    }

    @GetMapping(value = { "/center/pdf/{centerId}" }, produces = { "application/pdf" })
    public byte[] generateCenterPdfReport(@PathVariable Long centerId) {
        return centerReportGenerationService.getPdfOutput(centerService.getCenterProfileById(centerId));
    }

    @GetMapping(value = { "/club/pdf/resultsearch" }, produces = { "application/pdf" })
    public byte[] generateResultSearchPdfReport(SearchClubProfile searchClubProfile,
                                                @PageableDefault(sort = "id", value = 50) Pageable pageable) {
        return resultSearchReportGenerationService
                .getPdfOutput(clubService.getClubsBySearchParameters(searchClubProfile, pageable));
    }
}
