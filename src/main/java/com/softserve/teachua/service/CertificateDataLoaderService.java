package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificate.CertificateDatabaseResponse;

import java.util.List;

/**
 * This interface contains all needed methods to manage certificate data loader.
 */
public interface CertificateDataLoaderService {

    /**
     * The method loads data from excel to database.
     */
    List<CertificateDatabaseResponse> saveToDatabase(CertificateDataRequest dates);
}
