package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ArchiveMark<T> {
    void archiveModel(T t);

    void restoreModel(String archiveObject) throws JsonProcessingException;
}
