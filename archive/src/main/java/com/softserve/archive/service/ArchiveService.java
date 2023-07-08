package com.softserve.archive.service;

import com.softserve.archive.model.Archive;
import java.util.Map;

public interface ArchiveService {
    void save(Archive archive);

    Map<String, String> restoreModel(String className, Number dataId);
}
