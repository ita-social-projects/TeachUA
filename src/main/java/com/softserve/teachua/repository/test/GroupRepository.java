package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByEnrollmentKey(String enrollmentKey);
    @Query("SELECT gt.group FROM GroupTest gt WHERE gt.test.id = :id")
    List<Group> findByTestId(Long id);
}
