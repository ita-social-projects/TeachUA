package com.softserve.teachua.repository;

import com.softserve.teachua.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findById(Long id);

    List<Complaint> getAllByClubUserId(Long userId);

    List<Complaint> getAllByClubId(Long clubId);

    void deleteById(Long id);
}
