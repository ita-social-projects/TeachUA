package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;

import java.io.IOException;
import java.io.InputStream;

public interface ExcelCertificateService {
    ExcelParsingResponse parseExcel(InputStream excelInputStream);
}
