package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.certificate.*;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.Message;
import com.softserve.teachua.model.Messenger;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.repository.MessengerRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.CertificateByTemplateService;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.utils.CertificateContentDecorator;
import com.softserve.teachua.utils.QRCodeService;
import java.io.*;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    private static final String CERTIFICATE_NOT_FOUND_BY_USERNAME_AND_DATES =
        "Certificate not found by username and dates: %s, %s";

    private final DtoConverter dtoConverter;
    private final QRCodeService qrCodeService;
    private final CertificateRepository certificateRepository;
    private final CertificateContentDecorator certificateContentDecorator;
    private final CertificateByTemplateService certificateByTemplateService;
    private final MessengerRepository messengerRepository;

    @Autowired
    public CertificateServiceImpl(DtoConverter dtoConverter, QRCodeService qrCodeService,
                                  CertificateRepository certificateRepository, CertificateContentDecorator certificateContentDecorator,
                                  CertificateByTemplateService certificateByTemplateService, MessengerRepository messengerRepository) {
        this.dtoConverter = dtoConverter;
        this.qrCodeService = qrCodeService;
        this.certificateRepository = certificateRepository;
        this.certificateContentDecorator = certificateContentDecorator;
        this.certificateByTemplateService = certificateByTemplateService;
        this.messengerRepository = messengerRepository;
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
    public List<CertificateUserResponse> getListOfCertificatesByEmail(String email) {
        return certificateRepository.findAllForDownload(email)
                .stream()
                .map(certificate -> CertificateUserResponse.builder()
                                .id(certificate.getId())
                                .serialNumber(certificate.getSerialNumber())
                                .certificateType(certificate.getTemplate().getCertificateType())
                                .date(certificate.getDates().getDate())
                                .courseDescription(certificate.getTemplate().getCourseDescription())
                                .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Certificate> getSentCertificatesByEmailAndUpdateStatus(String sendToEmail, LocalDate updateStatus) {
        return certificateRepository.findAllBySendToEmailAndUpdateStatusAndSendStatusTrue(sendToEmail, updateStatus);
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
            String initialNumber;

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

        if (transfer.getSerialNumber() == null && transfer.getUpdateStatus() == null) {
            transfer = updateCertificateWithSerialNumber(transfer.getId(), transfer);
        }
        if (transfer.getTemplate().getId() <= 3) {
            CertificateContent content = CertificateContent.builder().id(transfer.getId())
                    .serialNumber(transfer.getSerialNumber()).issuanceDate(transfer.getDates().getDate())
                    .userName(transfer.getUserName()).studyDuration(transfer.getDates().getDuration()).build();
            content.setStudyHours(certificateContentDecorator.formHours(transfer.getDates().getHours()));
            content.setQrCode(qrCodeService.getCertificateQrCodeAsStream(content.getSerialNumber()));
            content.setStudyForm(transfer.getDates().getStudyForm());
            try {
                JasperPrint jasperPrint = createJasperPrint(transfer.getTemplate().getFilePath(), content);
                JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            } catch (IOException | JRException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File file = new File(certificateByTemplateService.createCertificateByTemplate(transfer));
                Path filePath = file.toPath();
                byte[] bytes = Files.readAllBytes(filePath);
                Files.delete(filePath);
                return bytes;
            } catch (IOException e) {
                log.error("Error creating certificate by template");
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    @Override
    public byte[] getPdfOutputForDownload(String userEmail, Long id) {
        Certificate certificate = getCertificateById(id);
        boolean isSent = Optional.ofNullable(certificate.getSendStatus()).orElse(false);

        if (!certificate.getSendToEmail().equals(userEmail)) {
            throw new AccessDeniedException("Forbidden");
        } else if (isSent && certificate.getSerialNumber() == null && certificate.getUpdateStatus() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Requested certificate has no serial number");
        }
        CertificateTransfer certificateTransfer = dtoConverter.convertToDto(certificate, CertificateTransfer.class);
        // first get pdf, then update status
        byte[] pdfOutput = getPdfOutput(certificateTransfer);
        if (!isSent) {
            updateDateAndSendStatus(certificate.getId(), true);
        }
        return pdfOutput;
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
        if (!certificatePreview.getUserName().equals(certificate.getUserName())) {
            certificate.setUserName(certificatePreview.getUserName());
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
    public List<CertificatePreview> getSimilarCertificatesByUserName(String userName) {
        return certificateRepository.findSimilarByUserName(userName).stream().map(
                certificate -> (CertificatePreview) dtoConverter.convertToDto(certificate, CertificatePreview.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<Certificate> getByMessengerId(String id) {
        Optional<Messenger> messenger = messengerRepository.findByAccessKey(id);
        if (messenger.isPresent()) return certificateRepository.findByUserName(messenger.get().getUserName());
        return new ArrayList<>();
    }

    @Override
    public boolean existByEmail(String email) {
        return certificateRepository.existsBySendToEmail(email);
    }

    @Override
    public boolean existByEmailAndName(String email, String name) {
        return certificateRepository.existsByUserNameAndSendToEmail(name, email);
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
