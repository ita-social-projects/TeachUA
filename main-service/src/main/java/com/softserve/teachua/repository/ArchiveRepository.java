package com.softserve.teachua.repository;

import com.softserve.teachua.model.Archive;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface to manage {@link Archive} archive.
 */
@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    List<Archive> findAllByClassName(String className);
}
