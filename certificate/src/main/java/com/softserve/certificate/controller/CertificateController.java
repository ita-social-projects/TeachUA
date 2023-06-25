package com.softserve.certificate.controller;

import com.softserve.certificate.dto.certificate.CertificateDataRequest;
import com.softserve.certificate.dto.certificate.CertificateDatabaseResponse;
import com.softserve.certificate.dto.certificate.CertificatePreview;
import com.softserve.certificate.dto.certificate.CertificateVerificationResponse;
import com.softserve.certificate.dto.certificate_excel.ExcelParsingResponse;
import com.softserve.certificate.security.UserPrincipal;
import com.softserve.certificate.service.CertificateDataLoaderService;
import com.softserve.certificate.service.CertificateExcelService;
import com.softserve.certificate.service.CertificateService;
import com.softserve.certificate.utils.annotation.AllowedRoles;
import com.softserve.commons.certificate.dto.CertificateUserResponse;
import com.softserve.commons.constant.RoleData;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * This controller is responsible for managing certificates.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/certificate")
public class CertificateController {
    private final CertificateService certificateService;
    private final CertificateExcelService excelService;
    private final CertificateDataLoaderService loaderService;

    public CertificateController(CertificateService certificateService, CertificateExcelService excelService,
                                 CertificateDataLoaderService loaderService) {
        this.certificateService = certificateService;
        this.excelService = excelService;
        this.loaderService = loaderService;
    }

    /**
     * This endpoint is used to get the number of unsent certificates.
     *
     * @return number of unsent certificates
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/generate")
    public Integer getUnsentCertificates() {
        return certificateService.getListOfUnsentCertificates().size();
    }

    /**
     * The method uploads excel file and returns {@code ExcelParsingResponse}.
     *
     * @param multipartFile - excel file.
     * @return new {@code ExcelParsingResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/excel")
    public ExcelParsingResponse uploadExcel(@RequestParam("excel-file") MultipartFile multipartFile) {
        return excelService.parseExcel(multipartFile);
    }

    /**
     * The method saves data to database.
     *
     * @param data - {@code CertificateDataToDatabase} read from form.
     * @return new {@code List<CertificateDatabaseResponse>}
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping
    public List<CertificateDatabaseResponse> saveExcel(@Valid @RequestBody CertificateDataRequest data) {
        log.info("Save excel " + data);
        return loaderService.saveToDatabase(data);
    }

    /**
     * This endpoint is used to validate the certificate, user passes serial number into url, this controller returns.
     * {@code CertificateVerificationResponse}
     *
     * @param serialNumber - serial number of certificate, being verified
     * @return new {@code CertificateVerificationResponse}
     */
    @GetMapping("/{serialNumber}")
    public CertificateVerificationResponse validateCertificate(@PathVariable("serialNumber") Long serialNumber) {
        return certificateService.validateCertificate(serialNumber);
    }

    /**
     * This endpoint is used to get the information about all sent certificates.
     *
     * @return {@code List<CertificatePreview>}
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping
    public List<CertificatePreview> getAllCertificates() {
        return certificateService.getListOfCertificatesPreview();
    }

    /**
     * This endpoint is used to get all certificates of authenticated user.
     *
     * @return {@code List<CertificateUserResponse>}
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(params = {"email"})
    public List<CertificateUserResponse> getListOfCertificatesByUserEmail(@RequestParam("email") String email) {
        return certificateService.getListOfCertificatesByUserEmail(email);
    }

    /**
     * This endpoint is used to download certificate, requested by owner.
     *
     * @return {@code ResponseEntity<byte[]>}
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable("id") Long id) {
        UserPrincipal userPrincipal =
                (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        byte[] bytes = certificateService.getPdfOutputForDownload(userPrincipal.getUsername(), id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificate.pdf");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @AllowedRoles(RoleData.ADMIN)
    @GetMapping(params = {"userName"})
    public List<CertificatePreview> searchCertificatesUser(@RequestParam(name = "userName") String userName) {
        return certificateService.getSimilarCertificatesByUserName(userName);
    }

    /**
     * This endpoint is used to get update certificate.
     *
     * @param id                 - put certificate id
     * @param certificatePreview - {@code CertificatePreview} to update
     * @return {@code List<CertificatePreview>}
     */
    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/{id}")
    public CertificatePreview updateCertificate(@PathVariable Long id,
                                                @Valid @RequestBody CertificatePreview certificatePreview) {
        return certificateService.updateCertificatePreview(id, certificatePreview);
    }
}
