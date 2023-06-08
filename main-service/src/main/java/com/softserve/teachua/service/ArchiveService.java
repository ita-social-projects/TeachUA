package com.softserve.teachua.service;

import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.exception.RestoreArchiveException;
import com.softserve.teachua.model.Archive;
import com.softserve.commons.marker.Archivable;
import java.util.List;

/**
 * This interface contains all needed methods to manage archive using ArchiveRepository.
 */

public interface ArchiveService {
    /**
     * The method returns list of entities {@code List<Archive>} of all existing archive found by id.
     *
     * @param className - put Archive name.
     * @return new {@code List<Archive>}.
     */
    List<Archive> findArchivesByClassName(String className);

    /**
     * The method returns list of entities {@code List<Archive>} of all archive.
     *
     * @return new {@code List<Archive>}.
     */
    List<Archive> findAllArchives();

    /**
     * The method saves model into archive and returns this archive.
     *
     * @param archiveModel - place body of entity {@code Archivable}.
     * @return {@code Archive}.
     */
    Archive saveModel(Archivable archiveModel);

    /**
     * The method restore model from archive and returns archive.
     *
     * @param id - put archive id
     * @return {@code Archive}.
     * @throws RestoreArchiveException if process failed
     */
    Archive restoreArchiveObject(Long id);

    /**
     * The method returns entity {@code Archive} of archive by id.
     *
     * @param id - put archive id.
     * @return new {@code Archive}.
     * @throws NotExistException if archive not exists.
     */
    Archive getArchiveObjectById(Long id);
}
