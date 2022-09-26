package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.certificate.*;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateExcelService;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This controller is responsible for managing certificates
 */
@RestController
@Slf4j
public class CertificateController implements Api {
    private final CertificateService certificateService;
    private final CertificateExcelService excelService;
    private final CertificateDataLoaderService loaderService;

    @Autowired
    public CertificateController(CertificateService certificateService, CertificateExcelService excelService,
            CertificateDataLoaderService loaderService) {
        this.certificateService = certificateService;
        this.excelService = excelService;
        this.loaderService = loaderService;
    }

    /**
     * This endpoint is used to get the number of unsent certificates
     *
     * @return number of unsent certificates
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/certificate/generate")
    public Integer getUnsentCertificates() {
        return certificateService.getListOfUnsentCertificates().size();
    }

    /**
     * The method uploads excel file and returns {@code ExcelParsingResponse}.
     *
     * @param multipartFile
     *            - excel file.
     *
     * @return new {@code ExcelParsingResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate/excel")
    public ExcelParsingResponse uploadExcel(@RequestParam("excel-file") MultipartFile multipartFile) {
        return excelService.parseExcel(multipartFile);
    }

    /**
     * The method saves data to database.
     *
     * @param data
     *            - {@code CertificateDataToDatabase} read from form.
     *
     * @return new {@code List<CertificateDatabaseResponse>}
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate/load-to-db")
    public List<CertificateDatabaseResponse> saveExcel(@Valid @RequestBody CertificateDataRequest data) {
        log.info("Save excel " + data);
        return loaderService.saveToDatabase(data);
    }

    /**
     * This endpoint is used to validate the certificate, user passes serial number into url, this controller returns
     * {@code CertificateVerificationResponse}
     *
     * @param serialNumber
     *            - serial number of certificate, being verified
     *
     * @return new {@code CertificateVerificationResponse}
     */
    @GetMapping("/certificate/{serialNumber}")
    public CertificateVerificationResponse validateCertificate(@PathVariable("serialNumber") Long serialNumber) {
        return certificateService.validateCertificate(serialNumber);
    }

    /**
     * This endpoint is used to get the information about all sent certificates.
     *
     * @return {@code List<CertificatePreview>}
     */
    @GetMapping("/certificates")
    public List<CertificatePreview> getAllCertificates() {
        return certificateService.getListOfCertificatesPreview();
    }

    @GetMapping("/certificate")
    public List<CertificatePreview> searchCertificatesUser(@RequestParam(name="userName") String userName){
        return certificateService.getSimilarCertificatesByUserName(userName);
    }

    /**
     * This endpoint is used to get update certificate.
     *
     * @param id
     *            - put certificate id
     * @param certificatePreview
     *            - {@code CertificatePreview} to update
     *
     * @return {@code List<CertificatePreview>}
     */
    @PutMapping("/certificates/{id}")
    public CertificatePreview updateCertificate(@PathVariable Long id,
            @Valid @RequestBody CertificatePreview certificatePreview) {
        return certificateService.updateCertificatePreview(id, certificatePreview);
    }
}
