package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.marker.Archivable;

import java.util.List;

/**
 * This interface contains all needed methods to manage archive using ArchiveRepository.
 */

public interface ArchiveService {
    /**
     * The method returns list of entities {@code List<Archive>} of all existing archives found by id.
     *
     * @param className - put Archive name.
     * @return new {@code List<Archive>}.
     */
    List<Archive> findArchivesByClassName(String className);

    /**
     * The method returns list of entities {@code List<Archive>} of all archives.
     *
     * @return new {@code List<Archive>}.
     */
    List<Archive> findAllArchives();

    /**
     * The method saves model into archive and returns the type of the model.
     *
     * @param archiveModel - place body of entity {@code Archivable}.
     * @return {@code Archive}.
     */
    Archive saveModel(Archivable archiveModel);

    Archive restoreArchiveObject(Long id) throws ClassNotFoundException, JsonProcessingException;

    Archive getArchiveObjectById(Long id);

}
