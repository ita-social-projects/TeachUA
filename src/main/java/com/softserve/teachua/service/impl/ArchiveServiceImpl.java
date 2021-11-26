package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.JsonWriteException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.repository.ArchiveRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {
    private static final String JSON_WRITE_EXCEPTION = "Model with name: %s can't be converted to json";
    private final ArchiveRepository archiveRepository;
    private final ObjectMapper objectMapper;

    private final ApplicationContext context;

    @Autowired
    public ArchiveServiceImpl(ArchiveRepository archiveRepository, ObjectMapper objectMapper, ApplicationContext context) {
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
    public <T extends Archivable> T saveModel(T model) {
        Archive archive;
        try {
            archive = Archive.builder()
                    .className(model.getClass().getSimpleName())
                    .data(objectMapper.writeValueAsString(model))
                    .build();
        } catch (JsonProcessingException e) {
            throw new JsonWriteException(String.format(JSON_WRITE_EXCEPTION, model.getClass().getName()));
        }
        log.debug("**/Model {} adding to archive", model.getClass().getName());
        archiveRepository.save(archive);
        return model;
    }

    @Override
    public Archive  restoreArchiveObject(Long id) {
        Archive archiveObject = getArchiveObjectById(id);
        ArchiveMark archiveMark = (ArchiveMark) context.getBean(
                archiveObject.getClassName().toLowerCase(Locale.ROOT).charAt(0)
                        + archiveObject.getClassName().substring(1)
                        + "ServiceImpl");
        archiveMark.restoreModel(archiveObject.getData());
        return archiveObject;
    }

    @Override
    public Archive getArchiveObjectById(Long id) {
        Optional<Archive> model = archiveRepository.findById(id);

        if(!model.isPresent()){
            throw new NotExistException();
        }

        return model.get();
    }
}
