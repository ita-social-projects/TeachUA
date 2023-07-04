package com.softserve.archive.repository;

import com.softserve.archive.model.Archive;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArchiveRepository extends MongoRepository<Archive, String> {
}
