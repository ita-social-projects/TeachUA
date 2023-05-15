package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.certificate.CertificateContent;
import com.softserve.teachua.dto.certificate.CertificatePreview;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.dto.certificate.CertificateUserResponse;
import com.softserve.teachua.dto.certificate.CertificateVerificationResponse;
import com.softserve.teachua.exception.CertificateGenerationException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.UserPermissionException;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.archivable.CertificateArch;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.CertificateByTemplateService;
import com.softserve.teachua.service.CertificateDatesService;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.utils.CertificateContentDecorator;
import com.softserve.teachua.utils.QRCodeService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
public class CertificateServiceImpl implements CertificateService, ArchiveMark<Certificate> {
    private static final String CERTIFICATE_NOT_FOUND_BY_ID = "Certificate not found by id %s";
    private static final String CERTIFICATE_NOT_FOUND_BY_SERIAL_NUMBER = "Certificate not found by serial number %s";
    private static final String CERTIFICATE_NOT_FOUND_BY_USERNAME_AND_DATES =
            "Certificate not found by username and dates: %s, %s";

    private final ObjectMapper objectMapper;
    private final DtoConverter dtoConverter;
    private final QRCodeService qrCodeService;
    private final ArchiveService archiveService;
    private final CertificateRepository certificateRepository;
    private final CertificateTemplateService certificateTemplateService;
    private final CertificateDatesService certificateDatesService;
    private final CertificateContentDecorator certificateContentDecorator;
    private final CertificateByTemplateService certificateByTemplateService;

    @Autowired
    public CertificateServiceImpl(ObjectMapper objectMapper, DtoConverter dtoConverter, QRCodeService qrCodeService,
                                  ArchiveService archiveService, CertificateRepository certificateRepository,
                                  CertificateTemplateService certificateTemplateService,
                                  CertificateDatesService certificateDatesService,
                                  CertificateContentDecorator certificateContentDecorator,
                                  CertificateByTemplateService certificateByTemplateService) {
        this.objectMapper = objectMapper;
        this.dtoConverter = dtoConverter;
        this.qrCodeService = qrCodeService;
        this.archiveService = archiveService;
        this.certificateRepository = certificateRepository;
        this.certificateTemplateService = certificateTemplateService;
        this.certificateDatesService = certificateDatesService;
        this.certificateContentDecorator = certificateContentDecorator;
        this.certificateByTemplateService = certificateByTemplateService;
    }

    @Override
    public List<CertificateTransfer> getListOfCertificates() {
        List<CertificateTransfer> certificateTransfers = certificateRepository.findAll().stream()
                .map(certificate ->
                        (CertificateTransfer) dtoConverter.convertToDto(certificate, CertificateTransfer.class))
                .toList();

        log.debug("getting list of certificates {}", certificateTransfers);
        return certificateTransfers;
    }

    @Override
    public List<CertificatePreview> getListOfCertificatesPreview() {
        return certificateRepository.findAllByOrderByIdAsc().stream()
                .map(certificate ->
                        (CertificatePreview) dtoConverter.convertToDto(certificate, CertificatePreview.class))
                .toList();
    }

    @Override
    public List<CertificateUserResponse> getListOfCertificatesByEmail(String email) {
        return certificateRepository.findAllForDownload(email)
                .stream()
                .map(certificate -> CertificateUserResponse.builder()
                        .id(certificate.getId())
                        .serialNumber(certificate.getSerialNumber())
                        .certificateTypeName(certificate.getTemplate().getCertificateType().getName())
                        .date(certificate.getDates().getDate())
                        .courseDescription(certificate.getTemplate().getCourseDescription())
                        .build())
                .toList();
    }

    @Override
    public List<Certificate> getSentCertificatesByEmailAndUpdateStatus(String sendToEmail, LocalDate updateStatus) {
        return certificateRepository.findAllBySendToEmailAndUpdateStatusAndSendStatusTrue(sendToEmail, updateStatus);
    }

    @Override
    public List<CertificateTransfer> getListOfUnsentCertificates() {
        List<CertificateTransfer> certificateTransfers = certificateRepository.findUnsentCertificates().stream()
                .map(certificate -> (CertificateTransfer) dtoConverter
                        .convertToDto(certificate, CertificateTransfer.class))
                .toList();

        log.debug("getting list of unsent certificates {}", certificateTransfers);
        return certificateTransfers;
    }

    @Override
    public CertificateTransfer getOneUnsentCertificate() {
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

        if (optionalCertificate.isEmpty()) {
            throw new NotExistException(String.format(CERTIFICATE_NOT_FOUND_BY_ID, id));
        }

        Certificate certificate = optionalCertificate.get();
        log.debug("getting certificate by id {}", certificate);
        return certificate;
    }

    @Override
    public Certificate getCertificateBySerialNumber(Long serialNumber) {
        Optional<Certificate> optionalCertificate = getOptionalCertificateBySerialNumber(serialNumber);

        if (optionalCertificate.isEmpty()) {
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

    private Optional<Certificate> getOptionalCertificateById(Long id) {
        return certificateRepository.findById(id);
    }

    private Optional<Certificate> getOptionalCertificateBySerialNumber(Long serialNumber) {
        return certificateRepository.findBySerialNumber(serialNumber);
    }

    @Override
    @SuppressWarnings("checkstyle:Indentation") //Suppressed because of unsupported switch style.
    public CertificateTransfer generateSerialNumber(CertificateTransfer response) {
        if (response.getTemplate().getCertificateType() == null || response.getDates().getCourseNumber() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Can't generate serial number for certificate, because certificateType or courseNumber is not set");
        }

        String courseNumber = String.format("%02d", Integer.valueOf(response.getDates().getCourseNumber()));

        Long largestSerialNumber = certificateRepository
                .findMaxSerialNumber(response.getTemplate().getCertificateType().getCodeNumber().toString());
        if (largestSerialNumber == null) {
            String initialNumber =
                    switch (response.getTemplate().getCertificateType().getCodeNumber()) {
                        case 1 -> "0000017";
                        case 2 -> "0000061";
                        default -> "0000001";
                    };

            response.setSerialNumber(Long.valueOf(
                    response.getTemplate().getCertificateType().getCodeNumber() + courseNumber + initialNumber));
        } else {
            String certificateNumber = String.format("%07d", largestSerialNumber + 1);
            response.setSerialNumber(Long.valueOf(
                    response.getTemplate().getCertificateType().getCodeNumber() + courseNumber + certificateNumber));
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
        if (transfer.getTemplate().getId() <= LAST_JRXML_TEMPLATE_ID) {
            CertificateContent content = CertificateContent.builder().id(transfer.getId())
                    .serialNumber(transfer.getSerialNumber()).issuanceDate(transfer.getDates().getDate())
                    .userName(transfer.getUserName()).studyDuration(transfer.getDates().getDuration()).build();
            content.setStudyHours(certificateContentDecorator.formHours(transfer.getDates().getHours()));
            content.setQrCode(qrCodeService.getCertificateQrCodeAsStream(content.getSerialNumber()));
            content.setStudyForm(transfer.getDates().getStudyForm());
            int hours = transfer.getDates().getHours();
            if (hours % 30 == 0) {
                content.setCredits(String.valueOf(hours / 30));
            } else {
                content.setCredits(String.valueOf(Math.round(hours / 30. * 10) / 10.));
            }
            try {
                JasperPrint jasperPrint = createJasperPrint(transfer.getTemplate().getFilePath(), content);
                JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                log.error("Cannot generate certificate for {}, {}", transfer, e.getMessage());
            }
        } else {
            try {
                File file = new File(certificateByTemplateService.createCertificateByTemplate(transfer));
                Path filePath = file.toPath();
                byte[] bytes = Files.readAllBytes(filePath);
                Files.delete(filePath);
                return bytes;
            } catch (IOException e) {
                log.error("Error creating certificate by template {}, {}", transfer, e.getMessage());
            }
        }
        throw new CertificateGenerationException();
    }

    @Override
    public byte[] getPdfOutputForDownload(String userEmail, Long id) {
        Certificate certificate = getCertificateById(id);
        boolean isSent = Optional.ofNullable(certificate.getSendStatus()).orElse(false);

        if (!certificate.getSendToEmail().equals(userEmail)) {
            throw new UserPermissionException();
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
                .certificateTypeName(certificate.getTemplate().getCertificateType().getName())
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

    private JRDataSource getDataSource(CertificateContent content) {
        return new JRBeanCollectionDataSource(Collections.singleton(content));
    }

    @Override
    public Certificate addCertificate(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Override
    public void addCertificates(List<Certificate> certificates) {
        certificates.forEach(this::addCertificate);
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
        return certificateRepository.findSimilarByUserName(userName).stream()
                .map(certificate -> (CertificatePreview) dtoConverter
                        .convertToDto(certificate, CertificatePreview.class))
                .toList();
    }

    @Override
    public void archiveModel(Certificate certificate) {
        CertificateArch certificateArch = dtoConverter.convertToDto(certificate, CertificateArch.class);
        archiveService.saveModel(certificateArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        CertificateArch certificateArch = objectMapper.readValue(archiveObject, CertificateArch.class);
        Certificate certificate = Certificate.builder().build();
        certificate = dtoConverter.convertToEntity(certificateArch, certificate).withId(null);
        if (Optional.ofNullable(certificateArch.getTemplateId()).isPresent()) {
            certificate.setTemplate(certificateTemplateService.getTemplateById(certificateArch.getTemplateId()));
        }
        if (Optional.ofNullable(certificateArch.getDatesId()).isPresent()) {
            certificate.setDates(certificateDatesService.getCertificateDatesById(certificateArch.getDatesId()));
        }
        certificateRepository.save(certificate);
    }

    @Override
    public boolean existsByUserNameAndDates(String name, CertificateDates dates) {
        return certificateRepository.existsByUserNameAndDates(name, dates);
    }
}
