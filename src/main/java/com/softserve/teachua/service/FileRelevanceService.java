package com.softserve.teachua.service;

import java.util.Set;

/**
 * This service contains methods to manage files saved in db
 */
public interface FileRelevanceService {

    String ORPHANED_FILES_SEARCH_PATH = "upload";

    String[] ORPHANED_FILES_EXTENSIONS = {"png", "jpg"};

    /**
     * This method returns set of all file locations stored in db.
     * <br><br>
     * It traverses about_us, banner_item, certificate_template, challenge, club, contact_type,
     * news, task and user tables.
     * @return {@code Set<String>}
     */
    Set<String> getAllMentionedFiles();

    /**
     * This method returns set of all files that are not mentioned in db.
     * <br><br>
     * It looks for files stored in {@code ORPHANED_FILES_SEARCH_PATH}
     * <br>
     * with extension formats from {@code ORPHANED_FILES_EXTENSIONS}
     *
     * @return {@code Set<String>}
     */
    Set<String> getAllOrphanedFiles();

}
