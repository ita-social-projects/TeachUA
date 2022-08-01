package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByEnrollmentKey(String enrollmentKey);
}
