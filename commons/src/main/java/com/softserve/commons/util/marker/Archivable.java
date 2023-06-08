package com.softserve.commons.util.marker;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Mark interface for use ArchiveService class that adds a remote entity to the archive.
 */
public interface Archivable {
    /**
     * The method returns class of service which operates model.
     *
     * @return {Class}
     */
    @JsonIgnore
    <T> Class<T> getServiceClass();
}
