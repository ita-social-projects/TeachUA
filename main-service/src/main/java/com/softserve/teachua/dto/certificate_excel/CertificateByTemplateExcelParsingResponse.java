package com.softserve.teachua.dto.certificate_excel;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CertificateByTemplateExcelParsingResponse {
    private List<String> columnHeadersList;
    private List<List<String>> excelContent;
}
