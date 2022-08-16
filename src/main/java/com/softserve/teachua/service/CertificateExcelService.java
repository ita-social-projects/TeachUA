package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * This interface contains all needed methods to manage exel parser.
 */
public interface CertificateExcelService {

    /**
     * The method parses excel
     *
     * @return new {@code ExcelParsingResponse}.
     */
    ExcelParsingResponse parseExcel(MultipartFile multipartFile);
}
