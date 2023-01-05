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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CertificateDatesServiceImpl implements CertificateDatesService, ArchiveMark<CertificateDates> {
    private static final String DATE_NOT_FOUND_BY_ID = "Certificate dates not found by id: %s";
    private static final String DATE_NOT_FOUND_BY_DURATION = "Certificate dates not found by duration: %s";
    private static final String DATE_NOT_FOUND_BY_DATE = "Certificate dates not found by date: %s";
    private static final String DATE_NOT_FOUND_BY_DURATION_AND_DATE =
        "Certificate dates not found by duration and date: %s, %s";
    private static final String DATE_NOT_FOUND_BY_HOURS_AND_DATE =
        "Certificate dates not found by hours and date: %s, %s";

    private final CertificateDatesRepository certificateDatesRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CertificateDatesServiceImpl(CertificateDatesRepository certificateDatesRepository, DtoConverter dtoConverter
        , ArchiveService archiveService, ObjectMapper objectMapper) {
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
    public CertificateDates getCertificateDatesById(Integer id) {
        return certificateDatesRepository.findById(id)
            .orElseThrow(() -> new NotExistException(String.format(DATE_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public CertificateDates addCertificateDates(CertificateDates dates) {
        return certificateDatesRepository.save(dates);
    }

    @Override
    public CertificateDates getCertificateDatesByDate(String date) {
        return certificateDatesRepository.findByDate(date)
            .orElseThrow(() -> new NotExistException(String.format(DATE_NOT_FOUND_BY_DATE, date)));
    }

    public CertificateDates getCertificateDatesByHoursAndDate(Integer hours, String date) {
        return certificateDatesRepository.findByHoursAndDate(hours, date).orElseThrow(
            () -> new NotExistException(String.format(DATE_NOT_FOUND_BY_HOURS_AND_DATE, hours, date)));
    }

    @Override
    public CertificateDates getCertificateDatesByDuration(String duration) {
        return certificateDatesRepository.findByDuration(duration)
            .orElseThrow(() -> new NotExistException(String.format(DATE_NOT_FOUND_BY_DURATION, duration)));
    }

    @Override
    public CertificateDates getCertificateDatesByDurationAndDate(String duration, String date) {
        return certificateDatesRepository.findByDurationAndDate(duration, date).orElseThrow(
            () -> new NotExistException(String.format(DATE_NOT_FOUND_BY_DURATION_AND_DATE, duration, date)));
    }
}
