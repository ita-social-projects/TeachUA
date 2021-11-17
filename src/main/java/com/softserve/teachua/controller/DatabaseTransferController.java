package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.databaseTransfer.ExcelLoadSuccess;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingData;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingResponse;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.service.DataLoaderService;
import com.softserve.teachua.service.ExcelParserService;
import com.softserve.teachua.service.SqlDataExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@RestController
public class DatabaseTransferController implements Api {
    private static final String FILE_LOAD_EXCEPTION = "Could not load excel file";

    private final ExcelParserService excelParserService;
    private final SqlDataExportService sqlDataExportService;
    private final DataLoaderService dataLoaderService;


    @Autowired
    public DatabaseTransferController(ExcelParserService excelParserService, SqlDataExportService sqlDataExportService,
                                      DataLoaderService dataLoaderService) {
        this.excelParserService = excelParserService;
        this.sqlDataExportService = sqlDataExportService;
        this.dataLoaderService = dataLoaderService;
    }

    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @PostMapping("/upload-excel")
    public ExcelParsingResponse uploadExcel(@RequestParam("excel-file") MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return excelParserService.parseExcel(inputStream);
        } catch (IOException ioe) {
            throw new FileUploadException(FILE_LOAD_EXCEPTION);
        }
    }

    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @PostMapping("/load-excel-to-db")
    public ExcelLoadSuccess loadExecelToDatabase(@RequestBody ExcelParsingData dataToLoad) {
        dataLoaderService.loadToDatabase(dataToLoad);
        return null;

    }

    @PreAuthorize("hasAnyRole(T(com.softserve.teachua.constants.RoleData).ADMIN.getDBRoleName())")
    @GetMapping("/download-database-sql")
    public ResponseEntity<Resource> download(String param) throws SQLException {
        String sqlScript = sqlDataExportService.createScript();
        byte[] bytes = sqlScript.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(bytes);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.sql.txt");


        return ResponseEntity.ok()
                .headers(header)
                .contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
