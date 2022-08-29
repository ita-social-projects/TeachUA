package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate.CertificateContent;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.dto.certificate.CertificateVerificationResponse;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import java.util.List;
import java.util.Map;

/**
 * This service contains methods to manage certificates
 */

public interface CertificateService {

    /**
     * This method returns list of dto {@code CertificateTransfer} of all certificates
     *
     * @return new {@code List<CertificateTransfer>}
     */
    List<CertificateTransfer> getListOfCertificates();

    /**
     * This method returns list of dto {@code CertificateTransfer} of all unsent
     * certificates (certificates with send status set to false)
     *
     * @return new {@code List<CertificateTransfer>}
     */
    List<CertificateTransfer> getListOfUnsentCertificates();

    /**
     * This method accepts no params and finds one unsent certificate in db
     *
     * @return new {@code CertificateTransfer}
     */
    CertificateTransfer getOneUnsentCertificate();

    /**
     * This method returns entity of {@code Certificate}, found by id
     *
     * @param id put Certificate id
     * @return new {@code Certificate}
     */
    Certificate getCertificateById(Long id);


    /**
     * This method returns entity of {@code Certificate}, found by serialNumber
     *
     * @param serialNumber put Certificate id
     * @return new {@code Certificate}
     */
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
     * Method generates serial number and puts it into the dto {@code CertificateTransfer}
     *
     * @param response put dto {@code CertificateTransfer} to update with serial number
     * @return new {@code CertificateTransfer} with filled serial number
     */
    CertificateTransfer generateSerialNumber(CertificateTransfer response);


    /**
     * This method updates {@code Certificate} with specified id in database
     * with data, provided in dto, returns dto of updated Certificate
     *
     * @param id put Certificate id
     * @return new {@code CertificateTransfer}
     */
    CertificateTransfer updateCertificateWithSerialNumber(Long id, CertificateTransfer response);

    /**
     * This method saves entity {@code Certificate} into database
     *
     * @param certificate entity to save
     * @return new {@code Certificate}
     */
    Certificate addCertificate(Certificate certificate);

    /**
     * This method updates email of certificate in database,
     * returns entity {@code Certificate} of updated certificate
     *
     * @param id id of certificate to update
     * @param certificate entity, containing data
     * @return new {@code Certificate}
     */
    Certificate updateCertificateEmail(Long id, Certificate certificate);

    /**
     * This method prepares map with parameters {@code Map<String, Object>}
     * for jasper to generate certificate
     *
     * @param content dto {@code CertificateContent} with parameters to put into certificate
     * @return new {@code Map<String, Object>}
     */
    Map<String, Object> getParameters(CertificateContent content);

    /**
     * This method gets the dto {@code CertificateTransfer}, prepares it
     * and then generates certificate, returns it in form of {@code byte[]}
     *
     * @param response put dto with certificate parameters
     * @return filled {@code byte[]}
     */
    byte[] getPdfOutput(CertificateTransfer response);


    /**
     * This method gets the serial number, obtains certificate with specified serial number from DB,
     * then if certificate exists, returns filled dto with info, in other case returns empty dto
     *
     * @param serialNumber put serial number to verify
     * @return filled {@code byte[]}
     */
    CertificateVerificationResponse validateCertificate(Long serialNumber);

    /**
     * This method updates {@code Certificate} with specified id in database
     * with given send status
     *
     * @param id put Certificate id
     * @param status put new status
     */
    void updateDateAndSendStatus(Long id, boolean status);
}
