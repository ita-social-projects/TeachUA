package com.softserve.teachua.dto.certificateExcel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelParsingResponse {

    private List<ExcelParsingMistake> parsingMistakes = new ArrayList<>();
    private List<CertificateExcel> certificatesInfo = new ArrayList<>();

}
