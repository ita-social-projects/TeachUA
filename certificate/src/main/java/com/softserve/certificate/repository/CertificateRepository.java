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
            SELECT *
            FROM certificate.certificates
            WHERE user_email = :email
            AND NOT (serial_number IS NULL AND update_status IS NOT NULL)
            ORDER BY update_status DESC
            """,
            nativeQuery = true)
    List<Certificate> findAllForDownload(@Param("email") String email);

    List<Certificate> findAllByUserEmailAndUpdateStatusAndSendStatusTrue(String sendToEmail, LocalDate updateDate);

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
