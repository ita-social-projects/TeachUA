package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificateExcel.CertificateByTemplateExcelParsingResponse;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
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

    CertificateByTemplateExcelParsingResponse parseFlexibleExcel(MultipartFile multipartFile);
}
