package com.softserve.teachua.service;

import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.marker.Archivable;

import java.util.List;

public interface ArchiveService {
    List<Archive> findArchivesByClassName(String className);
    List<Archive> findAllArchives();
    <T extends Archivable> T saveModel(T model);
}
