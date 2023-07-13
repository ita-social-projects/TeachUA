package com.softserve.certificate.repository;

import com.softserve.certificate.model.Certificate;
import com.softserve.certificate.model.CertificateDates;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    @Query(value = "SELECT certificate from Certificate AS certificate WHERE certificate.sendStatus IS NULL")
    List<Certificate> findUnsentCertificates();

    Certificate findTopBySendStatusNullOrderByIdAsc();

    boolean existsByTemplateId(Integer id);

    boolean existsByUserNameAndDates(String name, CertificateDates dates);

    List<Certificate> findAllByOrderByIdAsc();

    @Query(value = """
            FROM Certificate
            WHERE sendToEmail = :email
            AND NOT (serialNumber IS NULL AND updateStatus IS NOT NULL)
            ORDER BY updateStatus DESC
            """)
    List<Certificate> findAllForDownload(@Param("email") String email);

    List<Certificate> findAllBySendToEmailAndUpdateStatusAndSendStatusTrue(String sendToEmail, LocalDate updateDate);

    Optional<Certificate> findBySerialNumber(Long serialNumber);

    List<Certificate> findByUserName(String username);

    Optional<Certificate> findByUserNameAndDates(String username, CertificateDates dates);

    @Query(value = "SELECT MAX(SUBSTRING(CONCAT(t.serialNumber, ''), 4, 10)) from Certificate t "
            + "WHERE CONCAT(t.serialNumber, '') " + "LIKE CONCAT(:type, '%') ")
    Long findMaxSerialNumber(@Param("type") String type);

    @Query(value = "SELECT certificates from Certificate AS certificates "
            + "where LOWER(certificates.userName) LIKE LOWER(CONCAT('%', :username, '%')) ORDER BY certificates.id ASC")
    List<Certificate> findSimilarByUserName(@Param("username") String userName);
}
