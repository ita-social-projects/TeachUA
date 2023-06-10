package com.softserve.certificate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.certificate.dto.certificate.CertificateDataRequest;
import com.softserve.certificate.dto.certificate.CertificateDatabaseResponse;
import com.softserve.certificate.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.certificate.dto.googleapis.QuizResult;
import java.util.List;
import java.util.Map;

/**
 * This interface contains all needed methods to manage certificate data loader.
 */
public interface CertificateDataLoaderService {
    static String getCertificateByTemplateValue(Map<String, String> values, CertificateByTemplateTransfer data,
                                                int certificateIndex, String propertyTypeName, String propertyName) {
        String result = values.get(propertyName);
        if (result.trim().isEmpty()) {
            if (!data.getExcelContent().isEmpty()) {
                List<String> excelValues = data.getExcelContent().get(certificateIndex);
                result = excelValues.get(data.getColumnHeadersList()
                        .indexOf(data.getExcelColumnsOrder().get(data.getFieldsList().indexOf(propertyName))));
            } else if (!data.getGoogleFormResults().isEmpty()) {
                QuizResult quizResult = data.getGoogleFormResults().get(certificateIndex);
                if (propertyTypeName.equals("email")) {
                    result = quizResult.getUserEmail();
                } else if (propertyTypeName.equals("user_name")) {
                    result = quizResult.getFullName();
                }
            }
            values.put(propertyName, result);
        }
        return result;
    }

    /**
     * This method saves dto {@code CertificateDatabaseResponse} to database, returns list of dto
     * {@code List<CertificateDatabaseResponse>} of messages.
     *
     * @param data - dto read from excel-file and form on page to save
     * @return new {@code List<CertificateDatabaseResponse>}
     */
    List<CertificateDatabaseResponse> saveToDatabase(CertificateDataRequest data);

    void saveCertificate(CertificateByTemplateTransfer data) throws JsonProcessingException;
}
