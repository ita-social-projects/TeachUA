package com.softserve.teachua.model.marker;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Mark interface for use ArchiveService class that adds a remote entity to the archive.
 */
public interface Archivable {
    @JsonIgnore
    Class getServiceClass();
}
