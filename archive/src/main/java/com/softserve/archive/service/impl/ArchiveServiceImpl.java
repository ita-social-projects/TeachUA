package com.softserve.archive.service.impl;

import com.softserve.archive.model.Archive;
import com.softserve.archive.repository.ArchiveRepository;
import com.softserve.archive.service.ArchiveService;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ArchiveServiceImpl implements ArchiveService {
    private final ArchiveRepository archiveRepository;

    public ArchiveServiceImpl(ArchiveRepository archiveRepository) {
        this.archiveRepository = archiveRepository;
    }

    @Override
    public void save(Archive archive) {
        archiveRepository.save(archive);
    }

    @Override
    public Map<String, String> restoreModel(String className, Number dataId) {
        var result = archiveRepository.findByClassNameAndDataId(className, dataId.toString())
                .orElseThrow(IllegalArgumentException::new)
                .getData();
        archiveRepository.deleteByClassNameAndDataId(className, dataId.toString());

        return result;
    }
}
