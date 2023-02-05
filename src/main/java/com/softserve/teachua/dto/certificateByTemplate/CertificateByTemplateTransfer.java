package com.softserve.teachua.dto.certificateByTemplate;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateByTemplateTransfer {
    private List<String> fieldsList;
    private List<String> fieldPropertiesList;
    private String templateName;
    private String values;
    private List<String> columnHeadersList;
    private List<List<String>> excelContent;
    private List<String> excelColumnsOrder;
}
