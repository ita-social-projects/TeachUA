package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * This interface contains all needed methods to manage exel parser.
 */
public interface CertificateExcelService {

    /**
     * This method parses excel-file and returns {@code ExcelParsingResponse} of mistakes and created dto
     *
     * @param multipartFile
     *            - put bode of excel-file to parse
     *
     * @return new {@code ExcelParsingResponse}.
     */
    ExcelParsingResponse parseExcel(MultipartFile multipartFile);
}
