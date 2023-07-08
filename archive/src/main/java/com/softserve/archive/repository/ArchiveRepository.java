package com.softserve.archive.repository;

import com.softserve.archive.model.Archive;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ArchiveRepository extends MongoRepository<Archive, String> {
    @Query("{'className' : ?0, 'data.id' : ?1}")
    Optional<Archive> findByClassNameAndDataId(String className, String dataId);

    @Query(value = "{ 'className' : ?0, 'data.id' : ?1 }", delete = true)
    void deleteByClassNameAndDataId(String className, String dataId);
}
