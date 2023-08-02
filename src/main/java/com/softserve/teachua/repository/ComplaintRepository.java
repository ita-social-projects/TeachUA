package com.softserve.teachua.repository;

import com.softserve.teachua.model.Complaint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findById(Long id);

    List<Complaint> getAllByClubUserId(Long userId);

    List<Complaint> getAllByClubId(Long clubId);

    List<Complaint> getAllByRecipientId(Long recipientId);

    List<Complaint> getAllByUserId(Long senderId);

    void deleteById(Long id);
}
