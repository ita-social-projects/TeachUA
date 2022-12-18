package com.softserve.teachua.dto.certificateByTemplate;

import java.util.List;
import lombok.Data;

@Data
public class CertificateByTemplateTransfer {

    private List<String> fieldsList;
    private String templateName;
    private String values;
    private List<String> columnHeadersList;
    private List<List<String>> excelContent;
    private List<String> excelColumnsOrder;

}
