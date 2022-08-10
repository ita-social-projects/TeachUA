package com.softserve.teachua.repository;

import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findCertificatesByUser(User user);

    List<Certificate> findCertificatesByTemplate(CertificateTemplate template);

    Set<Certificate> deleteAllByUser(User user);

    boolean existsByUser(User user);

    boolean existsByUserEmail(String email);

    boolean existsBySerialNumber(Long serialNumber);
    boolean existsByUserName(String name);

    List<Certificate> findAll();

    Optional<Certificate> findById(Long id);

    Optional<Certificate> findBySerialNumber(Long serialNumber);
    Optional<Certificate> findByUserName(String username);

    @Query(value = "SELECT MAX(t.serialNumber) from Certificate t WHERE CONCAT(t.serialNumber, '') LIKE CONCAT(:type, :courseNumber, '%') ")
    Long findMaxSerialNumber(@Param("type") String type, @Param("courseNumber") String courseNumber);
}
