package com.softserve.teachua.service;

import com.softserve.teachua.model.marker.Archivable;

public interface ArchiveMark <T extends Archivable>{
    void archiveModel(T model);
    void restoreModel(String archiveObject);
}
