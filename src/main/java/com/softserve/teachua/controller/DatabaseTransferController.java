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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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

    @PostMapping("/upload-excel")
    public ExcelParsingResponse uploadExcel(@RequestParam("excel-file") MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return excelParserService.parseExcel(inputStream);
        } catch (IOException ioe) {
            throw new FileUploadException(FILE_LOAD_EXCEPTION);
        }
    }

    @PostMapping("/load-excel-to-db")
    public ExcelLoadSuccess loadExecelToDatabase(@RequestBody ExcelParsingData dataToLoad) {
        dataLoaderService.loadToDatabase(dataToLoad);
        return null;

    }

}
