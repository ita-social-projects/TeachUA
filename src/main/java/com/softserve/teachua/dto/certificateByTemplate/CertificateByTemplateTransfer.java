package com.softserve.teachua.dto.certificateByTemplate;

import java.util.List;
import lombok.Data;

@Data
public class CertificateByTemplateTransfer {

    private List<String> fieldsList;
    private List<String> inputtedValues;
    private String templateName;
    private List<String> columnHeadersList;
    private List<List<String>> excelContent;

}
