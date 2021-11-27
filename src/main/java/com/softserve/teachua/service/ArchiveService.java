package com.softserve.teachua.service;

import com.softserve.teachua.dto.archive.ArchiveProfile;
import com.softserve.teachua.model.Archive;

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
     * @param archiveProfile - place body of entity {@code ArchiveProfile}.
     * @return {@code Archive}.
     */
    Archive saveModel(ArchiveProfile archiveProfile);

    Archive restoreArchiveObject(Long id);

    Archive getArchiveObjectById(Long id);

}
