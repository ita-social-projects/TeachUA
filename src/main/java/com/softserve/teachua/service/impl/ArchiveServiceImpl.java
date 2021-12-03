package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.RestoreArchiveException;
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
import java.util.Optional;

@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {
    private static final String JSON_WRITE_EXCEPTION = "Model with name: %s can't be converted to json";
    private final ArchiveRepository archiveRepository;
    private final ObjectMapper objectMapper;

    private final ApplicationContext context;
    private final DtoConverter dtoConverter;

    @Autowired
    public ArchiveServiceImpl(ArchiveRepository archiveRepository, ObjectMapper objectMapper, ApplicationContext context, DtoConverter dtoConverter) {
        this.archiveRepository = archiveRepository;
        this.objectMapper = objectMapper;
        this.context = context;
        this.dtoConverter = dtoConverter;
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
        Optional <Archive> archive = Optional.empty();
        log.info("archiveModel: " + archiveModel);
        try {
            archive = Optional.of(Archive.builder()
                    .className(archiveModel.getServiceClass().getName())
                    .data(objectMapper.writeValueAsString(archiveModel))
                    .build());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return archiveRepository.save(archive.get());
    }

    @Override
    public Archive  restoreArchiveObject(Long id) {
        Archive archiveObject = getArchiveObjectById(id);
        try {
            ArchiveMark archiveMark = (ArchiveMark) context.getBean(Class.forName(archiveObject.getClassName()));
            archiveMark.restoreModel(archiveObject.getData());
        }catch (ClassNotFoundException exception) {
            log.error(RestoreArchiveException.CANT_FIND_CLASS, exception);
            throw new RestoreArchiveException(RestoreArchiveException.CANT_FIND_CLASS);
        }catch (JsonProcessingException exception) {
            log.error(RestoreArchiveException.CANT_READ_JSON, exception);
            throw new RestoreArchiveException(RestoreArchiveException.CANT_READ_JSON);
        }
        archiveRepository.deleteById(id);
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
