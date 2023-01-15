package com.softserve.teachua.service;

import com.softserve.teachua.dto.certificate.CertificateContent;
import com.softserve.teachua.dto.certificate.CertificatePreview;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.dto.certificate.CertificateUserResponse;
import com.softserve.teachua.dto.certificate.CertificateVerificationResponse;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * This service contains methods to manage certificates
 */

public interface CertificateService {
    /**
     * This const means the last database id of the certificate's template, which generates using Jasper .xml file
     */
    Integer LAST_JRXML_TEMPLATE_ID = 4;

    /**
     * This method returns list of dto {@code CertificateTransfer} of all certificates
     *
     * @return new {@code List<CertificateTransfer>}
     */
    List<CertificateTransfer> getListOfCertificates();

    /**
     * This method returns list of dto {@code CertificatePreview} of all certificates
     *
     * @return new {@code List<CertificatePreview>}
     */
    List<CertificatePreview> getListOfCertificatesPreview();

    /**
     * This method returns list of dto {@code CertificateUserResponse} of certificates, found by email,
     * except certificates without serial numbers
     *
     * @param sendToEmail user's email
     * @return new {@code List<CertificateUserResponse>}
     */
    List<CertificateUserResponse> getListOfCertificatesByEmail(String sendToEmail);

    /**
     * This method returns list of {@code Certificate}, found by sendToEmail and updateStatus with sendStatus true
     *
     * @param sendToEmail user's email
     * @param updateStatus date of sending
     * @return {@code List<Certificate>}
     */
    List<Certificate> getSentCertificatesByEmailAndUpdateStatus(String sendToEmail, LocalDate updateStatus);

    /**
     * This method returns list of dto {@code CertificateTransfer} of all unsent certificates (certificates with send
     * status set to false)
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
     * @param id
     *            put Certificate id
     *
     * @return new {@code Certificate}
     */
    Certificate getCertificateById(Long id);

    /**
     * This method returns entity of {@code Certificate}, found by serialNumber
     *
     * @param serialNumber
     *            put Certificate id
     *
     * @return new {@code Certificate}
     */
    Certificate getCertificateBySerialNumber(Long serialNumber);

    /**
     * The method returns {@link Certificate} by user name
     *
     * @param username
     *            put user name
     *
     * @return new {@code Certificate}
     */
    List<Certificate> getCertificatesByUserName(String username);

    /**
     * The method returns {@link Certificate} by user name and dates
     *
     * @param username
     *            put user name
     * @param dates
     *            put certificate dates
     *
     * @return new {@code Certificate}
     */
    Certificate getByUserNameAndDates(String username, CertificateDates dates);

    /**
     * Method generates serial number and puts it into the dto {@code CertificateTransfer}
     *
     * @param response
     *            put dto {@code CertificateTransfer} to update with serial number
     *
     * @return new {@code CertificateTransfer} with filled serial number
     */
    CertificateTransfer generateSerialNumber(CertificateTransfer response);

    /**
     * This method updates {@code Certificate} with specified id in database with data, provided in dto, returns dto of
     * updated Certificate
     *
     * @param id
     *            put Certificate id
     *
     * @return new {@code CertificateTransfer}
     */
    CertificateTransfer updateCertificateWithSerialNumber(Long id, CertificateTransfer response);

    /**
     * This method saves entity {@code Certificate} into database
     *
     * @param certificate
     *            entity to save
     *
     * @return new {@code Certificate}
     */
    Certificate addCertificate(Certificate certificate);

    /**
     * This method updates email of certificate in database, returns entity {@code Certificate} of updated certificate
     *
     * @param id
     *            id of certificate to update
     * @param certificate
     *            entity, containing data
     *
     * @return new {@code Certificate}
     */
    Certificate updateCertificateEmail(Long id, Certificate certificate);

    /**
     * This method updates certificate in database, returns dto {@code CertificatePreview} of updated certificate
     *
     * @param id
     *            id of certificate to update
     * @param certificatePreview
     *            dto, containing data
     *
     * @return new {@code CertificatePreview}
     */
    CertificatePreview updateCertificatePreview(Long id, CertificatePreview certificatePreview);

    /**
     * This method prepares map with parameters {@code Map<String, Object>} for jasper to generate certificate
     *
     * @param content
     *            dto {@code CertificateContent} with parameters to put into certificate
     *
     * @return new {@code Map<String, Object>}
     */
    Map<String, Object> getParameters(CertificateContent content);

    /**
     * This method gets the dto {@code CertificateTransfer}, prepares it and then generates certificate, returns it in
     * form of {@code byte[]}
     *
     * @param response
     *            put dto with certificate parameters
     *
     * @return filled {@code byte[]}
     */
    byte[] getPdfOutput(CertificateTransfer response);

    /**
     * This method gets the email and serial number, checks if user owns certificate and then generates one,
     * returns it in form of {@code byte[]}
     *
     * @param userEmail
     *            put authenticated user email
     * @param id
     *            put id of certificate
     *
     * @return filled {@code byte[]}
     */
    byte[] getPdfOutputForDownload(String userEmail, Long id);

    /**
     * This method gets the serial number, obtains certificate with specified serial number from DB, then if certificate
     * exists, returns filled dto with info, in other case returns empty dto
     *
     * @param serialNumber
     *            put serial number to verify
     *
     * @return filled
     */
    CertificateVerificationResponse validateCertificate(Long serialNumber);

    /**
     * This method updates {@code Certificate} with specified id in database with given send status
     *
     * @param id
     *            put Certificate id
     * @param status
     *            put new status
     */
    void updateDateAndSendStatus(Long id, boolean status);

    /**
     * This method takes userName and finds certificates with username partially or fully equal to param
     *
     * @param userName - put username
     *
     * @return - List of Certificates
     */
    List<CertificatePreview> getSimilarCertificatesByUserName(String userName);
}
