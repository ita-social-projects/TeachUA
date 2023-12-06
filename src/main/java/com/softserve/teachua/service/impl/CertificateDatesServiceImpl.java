package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.archivable.CertificateDatesArch;
import com.softserve.teachua.repository.CertificateDatesRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.CertificateDatesService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CertificateDatesServiceImpl implements CertificateDatesService, ArchiveMark<CertificateDates> {
    private static final String DATE_NOT_FOUND_BY_ID = "Certificate dates not found by id: %s";

    private final CertificateDatesRepository certificateDatesRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CertificateDatesServiceImpl(CertificateDatesRepository certificateDatesRepository, DtoConverter dtoConverter,
                                       ArchiveService archiveService, ObjectMapper objectMapper) {
        this.certificateDatesRepository = certificateDatesRepository;
        this.dtoConverter = dtoConverter;
        this.archiveService = archiveService;
        this.objectMapper = objectMapper;
    }

    @Override
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
    }

    @Override
    public boolean exists(CertificateDates certificateDates) {
        return certificateDatesRepository.existsByDateAndHoursAndDurationAndCourseNumberAndStudyForm(
                certificateDates.getDate(), certificateDates.getHours(), certificateDates.getDuration(),
                certificateDates.getCourseNumber(), certificateDates.getStudyForm());
    }

    @Override
    public CertificateDates getCertificateDatesById(Long id) {
        return certificateDatesRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(DATE_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public CertificateDates create(CertificateDates dates) {
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
        return findByExample(certificateDates).orElseGet(() ->
                certificateDatesRepository.save(certificateDates));
    }

    private Optional<CertificateDates> findByExample(CertificateDates certificateDates) {
        ExampleMatcher datesMatcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("id");

        Example<CertificateDates> datesExample = Example.of(certificateDates, datesMatcher);
        return certificateDatesRepository.findOne(datesExample);
    }
}
