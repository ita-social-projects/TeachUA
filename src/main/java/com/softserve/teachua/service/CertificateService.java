package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate.CertificateContent;
import com.softserve.teachua.dto.certificate.CertificateProfile;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.dto.certificate.CertificateVerificationResponse;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * this service contains methods for certificate generation
 */

public interface CertificateService {
    //TODO
    List<CertificateTransfer> getListOfCertificates();

    List<CertificateTransfer> getListOfUnsentCertificates();

    /**
     * Method finds {@link Certificate}
     *
     * @param id put Certificate id
     * @return new {@code Certificate}
     */
    Certificate getCertificateById(Long id);


    Certificate getCertificateBySerialNumber(Long serialNumber);

    /**
     * The method returns {@link Certificate} by user name
     *
     * @param username put user name
     * @return new {@code Certificate}
     */
    Certificate getCertificateByUserName(String username);

    /**
     * The method returns {@link Certificate} by user name and dates
     *
     * @param username put user name
     * @param dates put certificate dates
     * @return new {@code Certificate}
     */
    Certificate getByUserNameAndDates(String username, CertificateDates dates);

    /**
     * Method finds {@link CertificateTransfer}
     *
     * @param id put Certificate id
     * @return new {@code CertificateTransfer}
     */
    CertificateTransfer getCertificateProfileById(Long id);

    /**
     * Method generates serial number and puts it into the response
     *
     * @param response
     * @return response with filled serial number
     */
    CertificateTransfer generateSerialNumber(CertificateTransfer response);


    CertificateTransfer updateCertificateWithSerialNumber(Long id, CertificateTransfer response);

    Certificate addCertificate(Certificate certificate);

    Certificate updateCertificateEmail(Long id, Certificate certificate);

    Map<String, Object> getParameters(CertificateContent content) throws IOException;

    byte[] getPdfOutput(CertificateTransfer response);

    CertificateVerificationResponse validateCertificate(Long serialNumber);

    void updateDateAndSendStatus(Long id, boolean status);
}
