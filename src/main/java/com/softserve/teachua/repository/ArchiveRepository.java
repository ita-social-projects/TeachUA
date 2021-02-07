package com.softserve.teachua.repository;

import com.softserve.teachua.model.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Provides an interface to manage {@link Archive} archive
 */
@Repository
public interface ArchiveRepository extends JpaRepository <Archive, Long> {
    List<Archive> findAllByClassName(String className);
}
