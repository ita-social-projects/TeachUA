package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.certificate.CertificateContent;
import com.softserve.teachua.dto.certificate.CertificatePreview;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.dto.certificate.CertificateVerificationResponse;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.utils.CertificateContentDecorator;
import com.softserve.teachua.utils.QRCodeService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CertificateServiceImpl implements CertificateService, ArchiveMark<Certificate> {

    private static final String CERTIFICATE_NOT_FOUND_BY_ID = "Certificate not found by id %s";
    private static final String CERTIFICATE_NOT_FOUND_BY_SERIAL_NUMBER = "Certificate not found by serial number %s";
    private static final String CERTIFICATE_NOT_FOUND_BY_USERNAME_AND_DATES = "Certificate not found by username and dates: %s, %s";

    private final DtoConverter dtoConverter;
    private final ObjectMapper objectMapper;
    private final QRCodeService qrCodeService;
    private final CertificateRepository certificateRepository;
    private final CertificateContentDecorator certificateContentDecorator;

    @Autowired
    public CertificateServiceImpl(DtoConverter dtoConverter, ObjectMapper objectMapper, QRCodeService qrCodeService,
            CertificateRepository certificateRepository, CertificateContentDecorator certificateContentDecorator) {
        this.dtoConverter = dtoConverter;
        this.objectMapper = objectMapper;
        this.qrCodeService = qrCodeService;
        this.certificateRepository = certificateRepository;
        this.certificateContentDecorator = certificateContentDecorator;
    }

    @Override
    public List<CertificateTransfer> getListOfCertificates() {
        List<CertificateTransfer> certificateTransfers = certificateRepository.findAll().stream().map(
                certificate -> (CertificateTransfer) dtoConverter.convertToDto(certificate, CertificateTransfer.class))
                .collect(Collectors.toList());

        log.debug("getting list of certificates {}", certificateTransfers);
        return certificateTransfers;
    }

    @Override
    public List<CertificatePreview> getListOfCertificatesPreview() {
        return certificateRepository.findAllByOrderByIdAsc().stream().map(
                certificate -> (CertificatePreview) dtoConverter.convertToDto(certificate, CertificatePreview.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificateTransfer> getListOfUnsentCertificates() {

        List<CertificateTransfer> certificateTransfers = certificateRepository.findUnsentCertificates().stream().map(
                certificate -> (CertificateTransfer) dtoConverter.convertToDto(certificate, CertificateTransfer.class))
                .collect(Collectors.toList());

        log.debug("getting list of unsent certificates {}", certificateTransfers);
        return certificateTransfers;
    }

    @Override
    public CertificateTransfer getOneUnsentCertificate() {

        // Certificate certificate = certificateRepository.findOneUnsentCertificate();
        Certificate certificate = certificateRepository.findTopBySendStatusNullOrderByIdAsc();

        if (certificate == null) {
            return null;
        }

        log.debug("found unsent certificate: {}", certificate);
        return dtoConverter.convertToDto(certificate, CertificateTransfer.class);
    }

    @Override
    public Certificate getCertificateById(Long id) {
        Optional<Certificate> optionalCertificate = getOptionalCertificateById(id);

        if (!optionalCertificate.isPresent()) {
            throw new NotExistException(String.format(CERTIFICATE_NOT_FOUND_BY_ID, id));
        }

        Certificate certificate = optionalCertificate.get();
        log.debug("getting certificate by id {}", certificate);
        return certificate;
    }

    @Override
    public Certificate getCertificateBySerialNumber(Long serialNumber) {
        Optional<Certificate> optionalCertificate = getOptionalCertificateBySerialNumber(serialNumber);

        if (!optionalCertificate.isPresent()) {
            log.debug("certificate with serial number {} not found", serialNumber);
            throw new NotExistException(String.format(CERTIFICATE_NOT_FOUND_BY_SERIAL_NUMBER, serialNumber));
        }

        Certificate certificate = optionalCertificate.get();
        log.debug("getting certificate by serial number {}", certificate);
        return certificate;
    }

    @Override
    public List<Certificate> getCertificatesByUserName(String username) {
        return certificateRepository.findByUserName(username);
    }

    @Override
    public Certificate getByUserNameAndDates(String username, CertificateDates dates) {
        return certificateRepository.findByUserNameAndDates(username, dates).orElseThrow(() -> new NotExistException(
                String.format(CERTIFICATE_NOT_FOUND_BY_USERNAME_AND_DATES, username, dates)));
    }

    @Override
    public CertificateTransfer getCertificateProfileById(Long id) {
        Certificate certificate = getCertificateById(id);
        return dtoConverter.convertToDto(certificate, CertificateTransfer.class);
    }

    private Optional<Certificate> getOptionalCertificateById(Long id) {
        return certificateRepository.findById(id);
    }

    private Optional<Certificate> getOptionalCertificateBySerialNumber(Long serialNumber) {
        return certificateRepository.findBySerialNumber(serialNumber);
    }

    @Override
    public CertificateTransfer generateSerialNumber(CertificateTransfer response) {
        if (response.getTemplate().getCertificateType() == null || response.getDates().getCourseNumber() == null) {
            // exception
        }

        String courseNumber = String.format("%02d", Integer.valueOf(response.getDates().getCourseNumber()));

        Long largestSerialNumber = certificateRepository
                .findMaxSerialNumber(response.getTemplate().getCertificateType().toString());

        if (largestSerialNumber == null) {
            String initialNumber = null;

            switch (response.getTemplate().getCertificateType()) {
            case 1:
                initialNumber = "0000017";
                break;
            case 2:
                initialNumber = "0000061";
                break;
            case 3:
                initialNumber = "0000001";
                break;
            default:
                initialNumber = "0000001";
                break;
            }
            response.setSerialNumber(Long
                    .valueOf(response.getTemplate().getCertificateType().toString() + courseNumber + initialNumber));
        } else {
            String certificateNumber = String.format("%07d", largestSerialNumber + 1);
            response.setSerialNumber(Long.valueOf(
                    response.getTemplate().getCertificateType().toString() + courseNumber + certificateNumber));
        }

        return response;
    }

    @Override
    public CertificateTransfer updateCertificateWithSerialNumber(Long id, CertificateTransfer response) {
        Certificate certificate = getCertificateById(id);

        if (response.getSerialNumber() != null) {
            return response;
        }

        response = generateSerialNumber(response);

        Certificate newCertificate = dtoConverter.convertToEntity(response, certificate).withId(id)
                .withDates(certificate.getDates()).withSerialNumber(response.getSerialNumber())
                .withTemplate(certificate.getTemplate()).withUser(certificate.getUser())
                .withUserName(certificate.getUserName()).withSendToEmail(certificate.getSendToEmail());

        log.debug("updating serial number of certificate by id {}", newCertificate);

        return dtoConverter.convertToDto(certificateRepository.save(newCertificate), CertificateTransfer.class);
    }

    @Override
    public Map<String, Object> getParameters(CertificateContent content) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CONTENT", content);
        return parameters;
    }

    @Override
    public byte[] getPdfOutput(CertificateTransfer transfer) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if (transfer.getSerialNumber() == null) {
            // UNCOMMENT TO MAKE SERIAL NUMBERS WORK
            transfer = updateCertificateWithSerialNumber(transfer.getId(), transfer);
            //transfer.setSerialNumber(null); // REMOVE WHEN WE NEED SERIAL NUMBERS
        }

        CertificateContent content = CertificateContent.builder().id(transfer.getId())
                .serialNumber(transfer.getSerialNumber()).issuanceDate(transfer.getDates().getDate())
                .userName(transfer.getUserName()).studyDuration(transfer.getDates().getDuration()).build();

        content.setStudyHours(certificateContentDecorator.formHours(transfer.getDates().getHours()));
        content.setQrCode(qrCodeService.getCertificateQrCodeAsStream(content.getSerialNumber()));

        content.setStudyForm("дистанційна"); // TODO *****

        try {
            JasperPrint jasperPrint = createJasperPrint(transfer.getTemplate().getFilePath(), content);
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public CertificateVerificationResponse validateCertificate(Long serialNumber) {
        Certificate certificate = getCertificateBySerialNumber(serialNumber);

        CertificateVerificationResponse response = CertificateVerificationResponse.builder()
                .certificateType(certificate.getTemplate().getCertificateType())
                .courseDescription(certificate.getTemplate().getCourseDescription())
                .picturePath(certificate.getTemplate().getPicturePath())
                .projectDescription(certificate.getTemplate().getProjectDescription())
                .serialNumber(certificate.getSerialNumber()).userName(certificate.getUserName()).build();
        log.debug("verified certificate {}", certificate);

        return response;
    }

    public JasperPrint createJasperPrint(String templatePath, CertificateContent content)
            throws JRException, IOException {
        try (InputStream inputStream = new FileInputStream(certificateContentDecorator.getRealFilePath(templatePath))) {
            final JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            return JasperFillManager.fillReport(jasperReport, getParameters(content), getDataSource(content));
        }
    }

    private String getRealFilePath(final String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    private JRDataSource getDataSource(CertificateContent content) {
        return new JRBeanCollectionDataSource(Collections.singleton(content));
    }

    @Override
    public Certificate addCertificate(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Override
    public Certificate updateCertificateEmail(Long id, Certificate certificate) {
        Certificate certificateFound = getCertificateById(id);
        certificateFound.setSendToEmail(certificate.getSendToEmail());
        certificateFound.setSendStatus(certificate.getSendStatus());
        return certificateRepository.save(certificateFound);
    }

    @Override
    public CertificatePreview updateCertificatePreview(Long id, CertificatePreview certificatePreview) {
        Certificate certificate = getCertificateById(id);
        if (certificatePreview.getSendToEmail().equals(certificate.getSendToEmail())) {
            certificate.setSendStatus(certificatePreview.getSendStatus());
        } else {
            certificate.setSendToEmail(certificatePreview.getSendToEmail());
            certificate.setSendStatus(null);
        }
        return dtoConverter.convertToDto(certificateRepository.save(certificate), CertificatePreview.class);
    }

    @Override
    public void updateDateAndSendStatus(Long id, boolean status) {
        Certificate certificateById = getCertificateById(id);
        certificateById.setSendStatus(status);
        certificateById.setUpdateStatus(LocalDate.now());
        certificateRepository.save(certificateById);
    }

    @Override
    public List<CertificatePreview> getSimilarCertificatesByUserName(String userName){
        return certificateRepository.findSimilarByUserName(userName).stream().map(
                        certificate -> (CertificatePreview) dtoConverter.convertToDto(certificate, CertificatePreview.class))
                        .collect(Collectors.toList());
    }

    @Override
    public void archiveModel(Certificate certificate) {
        // TODO
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        // TODO
    }
}
