package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.RestoreArchiveException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.repository.ArchiveRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {
    private final ArchiveRepository archiveRepository;
    private final ObjectMapper objectMapper;

    private final ApplicationContext context;

    @Autowired
    public ArchiveServiceImpl(ArchiveRepository archiveRepository, ObjectMapper objectMapper,
                              ApplicationContext context) {
        this.archiveRepository = archiveRepository;
        this.objectMapper = objectMapper;
        this.context = context;
    }

    @Override
    public List<Archive> findArchivesByClassName(String className) {
        log.debug("**/finding list of Archives by class name = {}", className);
        return archiveRepository.findAllByClassName(className);
    }

    @Override
    public List<Archive> findAllArchives() {
        log.debug("**/getting list of all Archives");
        return archiveRepository.findAll();
    }

    @Override
    @Transactional
    public Archive saveModel(Archivable archiveModel) {
        Optional<Archive> archive;
        log.debug("archiveModel: " + archiveModel);
        try {
            archive = Optional.of(Archive.builder().className(archiveModel.getServiceClass().getName())
                    .data(objectMapper.writeValueAsString(archiveModel)).build());
        } catch (JsonProcessingException exception) {
            log.error(RestoreArchiveException.CANT_WRITE_JSON, exception);
            throw new RestoreArchiveException(RestoreArchiveException.CANT_WRITE_JSON);
        }
        return archiveRepository.save(archive.get());
    }

    @Override
    public Archive restoreArchiveObject(Long id) {
        Archive archiveObject = getArchiveObjectById(id);
        try {
            ArchiveMark<?> archiveMark = (ArchiveMark<?>) context.getBean(Class.forName(archiveObject.getClassName()));
            archiveMark.restoreModel(archiveObject.getData());
        } catch (ClassNotFoundException exception) {
            if (exception.getMessage().contains(ArchiveService.WRONG_ARCHIVE_CLASS_NAME)) {
                // Add message for unit tests
                log.error(RestoreArchiveException.CANT_FIND_CLASS, exception.toString());
            } else {
                log.error(RestoreArchiveException.CANT_FIND_CLASS, exception);
            }
            throw new RestoreArchiveException(RestoreArchiveException.CANT_FIND_CLASS);
        } catch (JsonProcessingException exception) {
            log.error(RestoreArchiveException.CANT_READ_JSON, exception);
            throw new RestoreArchiveException(RestoreArchiveException.CANT_READ_JSON);
        }
        archiveRepository.deleteById(id);
        return archiveObject;
    }

    @Override
    public Archive getArchiveObjectById(Long id) {
        Optional<Archive> model = archiveRepository.findById(id);

        if (model.isEmpty()) {
            throw new NotExistException();
        }

        return model.get();
    }
}
