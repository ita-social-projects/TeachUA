package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.database_transfer.ExcelParsingData;
import com.softserve.teachua.dto.database_transfer.ExcelParsingResponse;
import com.softserve.clients.exception.FileUploadException;
import com.softserve.teachua.service.DataLoaderService;
import com.softserve.teachua.service.ExcelParserService;
import com.softserve.teachua.service.SqlDataExportService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import io.swagger.v3.oas.annotations.Hidden;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * This controller is for managing the database transfering.
 */

@RestController
@Hidden
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

    /**
     * The method uploads excel file and returns {@code ExcelParsingResponse}.
     *
     * @param multipartFile
     *            - excel file.
     *
     * @return new {@code ExcelParsingResponse}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/upload-excel")
    public ExcelParsingResponse uploadExcel(@RequestParam("excel-file") MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return excelParserService.parseExcel(inputStream);
        } catch (IOException ioe) {
            throw new FileUploadException(FILE_LOAD_EXCEPTION);
        }
    }

    /**
     * The method loads data to database and returns {@code ExcelLoadSuccess}.
     *
     * @param dataToLoad
     *            - object of {@code ExcelParsingData}.
     *
     */
    @AllowedRoles(RoleData.ADMIN)
    @PostMapping("/load-excel-to-db")
    public void loadExcelToDatabase(@RequestBody ExcelParsingData dataToLoad) {
        dataLoaderService.loadToDatabase(dataToLoad);
    }

    /**
     * The method returns {@code ResponseEntity<Resource>}.
     *
     * @return new {@code ResponseEntity<Resource>}.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/download-database-sql")
    public ResponseEntity<Resource> download(String param) throws SQLException {
        String sqlScript = sqlDataExportService.createScript();
        byte[] bytes = sqlScript.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(bytes);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.sql.txt");

        return ResponseEntity.ok().headers(header).contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }
}
