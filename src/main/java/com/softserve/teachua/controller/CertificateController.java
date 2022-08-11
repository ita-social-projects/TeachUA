package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateExcelService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@Slf4j
public class CertificateController implements Api {
    private static final String FILE_LOAD_EXCEPTION = "Could not load excel file";
    private final CertificateExcelService excelService;
    private final CertificateDataLoaderService loaderService;

    @Autowired
    public CertificateController(CertificateExcelService excelService, CertificateDataLoaderService loaderService) {
        this.excelService = excelService;
        this.loaderService = loaderService;
    }

    /**
     * The method uploads excel file and returns {@code ExcelParsingResponse}.
     *
     * @param multipartFile - excel file.
     * @return new {@code ExcelParsingResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate/upload-excel")
    public ExcelParsingResponse uploadExcel(@RequestParam("excel-file") MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return excelService.parseExcel(inputStream);
        } catch (IOException e) {
            log.error("Upload excel error, " + FILE_LOAD_EXCEPTION);
            throw new RuntimeException(FILE_LOAD_EXCEPTION);
        }
    }

    /**
     * The method saves data to database.
     *
     * @param data - {@code CertificateDataToDatabase} read from form.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/certificate/load-to-db")
    public void saveExcel(@RequestBody CertificateDataRequest data) {
        log.info("Save excel " + data);
        loaderService.saveToDatabase(data);
    }
}
