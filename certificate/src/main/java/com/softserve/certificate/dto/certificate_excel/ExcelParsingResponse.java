package com.softserve.certificate.dto.certificate_excel;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelParsingResponse {
    //todo
    //private List<ExcelParsingMistake> parsingMistakes = new ArrayList<>();
    private List<CertificateExcel> certificatesInfo = new ArrayList<>();
}
