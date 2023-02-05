package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificate.CertificateDatabaseResponse;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import java.util.List;
import java.util.Map;

/**
 * This interface contains all needed methods to manage certificate data loader.
 */
public interface CertificateDataLoaderService {
    /**
     * This method saves dto {@code CertificateDatabaseResponse} to database, returns list of dto
     * {@code List<CertificateDatabaseResponse>} of messages.
     *
     * @param data - dto read from excel-file and form on page to save
     * @return new {@code List<CertificateDatabaseResponse>}
     */
    List<CertificateDatabaseResponse> saveToDatabase(CertificateDataRequest data);

    void saveCertificate(CertificateByTemplateTransfer data) throws JsonProcessingException;

    String getCertificateByTemplateValue(Map<String, String> values, List<String> fieldsList,
                                         List<String> columnHeadersList, List<String> excelColumnsOrder,
                                         List<String> excelValues, String propertyName);
}
