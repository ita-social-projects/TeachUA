package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.model.marker.Archivable;

public interface ArchiveMark<T> {
    void archiveModel(T t);

    void restoreModel(String archiveObject) throws JsonProcessingException;
}
