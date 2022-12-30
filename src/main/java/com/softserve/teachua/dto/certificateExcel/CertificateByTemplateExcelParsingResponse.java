package com.softserve.teachua.dto.certificateExcel;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CertificateByTemplateExcelParsingResponse {

    private List<String> columnHeadersList;
    private List<List<String>> excelContent;

}
