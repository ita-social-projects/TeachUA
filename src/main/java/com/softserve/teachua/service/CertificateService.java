package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate.CertificateResponse;
import com.softserve.teachua.model.Certificate;

import java.util.List;
import java.util.Map;

/**
 * this service contains methods for certificate generation
 */

public interface CertificateService {
    //TODO
    List<CertificateResponse> getListOfCertificates();

    /**
     * Method finds {@link Certificate}
     *
     * @param id put Certificate id
     * @return new {@code Certificate}
     */
    Certificate getCertificateById(Long id);


    Certificate getCertificateBySerialNumber(Long serialNumber);

    /**
     * Method finds {@link Certificate}
     *
     * @param username put User Name
     * @return new {@code Certificate}
     */
    Certificate getCertificateByUserName(String username);

    /**
     * Method finds {@link CertificateResponse}
     *
     * @param id put Certificate id
     * @return new {@code CertificateResponse}
     */
    CertificateResponse getCertificateProfileById(Long id);

    /**
     * Method generates serial number and puts it into the response
     *
     * @param response
     * @return response with filled serial number
     */
    CertificateResponse generateSerialNumber(CertificateResponse response);


    CertificateResponse updateCertificateWithSerialNumber(Long id, CertificateResponse response);

    Certificate createCertificate(Certificate certificate);

    Certificate updateCertificateEmail(Long id, Certificate certificate);
}
