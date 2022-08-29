package com.softserve.teachua.repository;

import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
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

    @Query(value = "SELECT certificate from Certificate AS certificate " +
            "WHERE certificate.sendStatus IS NULL")
    List<Certificate> findUnsentCertificates();

    @Query(value = "SELECT * FROM certificates AS certificate " +
            "WHERE certificate.send_status IS NULL " +
            "ORDER BY certificate.id " +
            "LIMIT 1", nativeQuery = true)
    Certificate findOneUnsentCertificate();

    Set<Certificate> deleteAllByUser(User user);

    boolean existsByUser(User user);

    boolean existsByUserEmail(String email);

    boolean existsBySerialNumber(Long serialNumber);

    boolean existsByUserName(String name);

    boolean existsByUserNameAndDates(String name, CertificateDates dates);

    List<Certificate> findAll();

    Optional<Certificate> findById(Long id);

    Optional<Certificate> findBySerialNumber(Long serialNumber);

    Optional<Certificate> findByUserName(String username);

    Optional<Certificate> findByUserNameAndDates(String username, CertificateDates dates);

    @Query(value = "SELECT MAX(SUBSTRING(CONCAT(t.serialNumber, ''), 4, 10)) from Certificate t " +
            "WHERE CONCAT(t.serialNumber, '') " +
            "LIKE CONCAT(:type, '%') ")
    Long findMaxSerialNumber(@Param("type") String type);
}
