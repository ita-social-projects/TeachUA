package com.softserve.certificate.service.impl;

import com.softserve.certificate.model.CertificateDates;
import com.softserve.certificate.repository.CertificateDatesRepository;
import com.softserve.certificate.service.CertificateDatesService;
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

    public CertificateDatesServiceImpl(CertificateDatesRepository certificateDatesRepository) {
        this.certificateDatesRepository = certificateDatesRepository;
    }

    //todo@
   /* @Override
    public void archiveModel(CertificateDates certificateDates) {
        CertificateDatesArch certificateDatesArch =
                dtoConverter.convertToDto(certificateDates, CertificateDatesArch.class);
        archiveService.saveModel(certificateDatesArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        CertificateDatesArch certificateDatesArch = objectMapper.readValue(archiveObject, CertificateDatesArch.class);
        certificateDatesRepository.save(
                dtoConverter.convertToEntity(certificateDatesArch, CertificateDates.builder().build()));
    }*/

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
