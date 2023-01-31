package com.softserve.teachua.dto.certificateExcel;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelParsingResponse {
    private List<ExcelParsingMistake> parsingMistakes = new ArrayList<>();
    private List<CertificateExcel> certificatesInfo = new ArrayList<>();
}
