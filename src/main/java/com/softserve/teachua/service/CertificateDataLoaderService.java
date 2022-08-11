package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate.CertificateDataRequest;

/**
 * This interface contains all needed methods to manage certificate data loader.
 */
public interface CertificateDataLoaderService {

    /**
     * The method loads data from excel to database.
     */
    void saveToDatabase(CertificateDataRequest dates);
}
