package com.softserve.certificate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.certificate.model.CertificateDates;
import com.softserve.certificate.repository.CertificateDatesRepository;
import com.softserve.certificate.service.CertificateDatesService;
import com.softserve.commons.client.ArchiveClient;
import com.softserve.commons.exception.NotExistException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("checkstyle:CommentsIndentation")
@Service
@Transactional
@Slf4j
public class CertificateDatesServiceImpl implements CertificateDatesService {
    private static final String DATE_NOT_FOUND_BY_ID = "Certificate dates not found by id: %s";

    private final CertificateDatesRepository certificateDatesRepository;
    private final ArchiveMQMessageProducer<CertificateDates> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;
    private final ObjectMapper objectMapper;


    public CertificateDatesServiceImpl(CertificateDatesRepository certificateDatesRepository,
                                       ArchiveMQMessageProducer<CertificateDates> archiveMQMessageProducer,
                                       ArchiveClient archiveClient, ObjectMapper objectMapper) {
        this.certificateDatesRepository = certificateDatesRepository;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
        this.objectMapper = objectMapper;
    }

    private void archiveModel(CertificateDates certificateDates) {
        archiveMQMessageProducer.publish(certificateDates);
    }

    @Override
    public void restoreModel(Integer id) {
        var certificateDates = objectMapper.convertValue(
                archiveClient.restoreModel(CertificateDates.class.getName(), id),
                CertificateDates.class);

        certificateDatesRepository.save(certificateDates);
    }

    @Override
    public boolean exists(CertificateDates certificateDates) {
        return certificateDatesRepository.existsByDateAndHoursAndDurationAndCourseNumberAndStudyForm(
                certificateDates.getDate(), certificateDates.getHours(), certificateDates.getDuration(),
                certificateDates.getCourseNumber(), certificateDates.getStudyForm());
    }

    @Override
    public CertificateDates getCertificateDatesById(Integer id) {
        return certificateDatesRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(DATE_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public CertificateDates addCertificateDates(CertificateDates dates) {
        return certificateDatesRepository.save(dates);
    }

    @Override
    public Optional<CertificateDates> findCertificateDates(CertificateDates certificateDates) {
        return certificateDatesRepository.findFirstByDateAndHoursAndDurationAndCourseNumberAndStudyForm(
                certificateDates.getDate(), certificateDates.getHours(), certificateDates.getDuration(),
                certificateDates.getCourseNumber(), certificateDates.getStudyForm());
    }

    @Override
    @Transactional
    public CertificateDates getOrCreateCertificateDates(CertificateDates certificateDates) {
        return findCertificateDates(certificateDates).orElseGet(() ->
                certificateDatesRepository.save(certificateDates));
    }
}
