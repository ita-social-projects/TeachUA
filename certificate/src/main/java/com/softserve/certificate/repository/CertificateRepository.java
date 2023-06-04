package com.softserve.certificate.repository;

import com.softserve.certificate.model.Certificate;
import com.softserve.certificate.model.CertificateDates;
import com.softserve.certificate.model.CertificateTemplate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    //todo
    //List<Certificate> findCertificatesByUser(User user);

    List<Certificate> findCertificatesByTemplate(CertificateTemplate template);

    @Query(value = "SELECT certificate from Certificate AS certificate WHERE certificate.sendStatus IS NULL")
    List<Certificate> findUnsentCertificates();

    Certificate findTopBySendStatusNullOrderByIdAsc();

    //Set<Certificate> deleteAllByUser(User user);
    //todo
    //boolean existsByUser(User user);
    //todo
    //boolean existsByUserEmail(String email);
    //
    //boolean existsBySerialNumber(Long serialNumber);
    //
    //boolean existsByUserName(String name);

    boolean existsByTemplateId(Integer id);

    boolean existsByUserNameAndDates(String name, CertificateDates dates);

    List<Certificate> findAll();

    List<Certificate> findAllByOrderByIdAsc();

    @Query(value = "SELECT * "
            + "FROM certificates "
            + "WHERE user_email = :email "
            + "AND NOT (serial_number IS NULL AND update_status IS NOT NULL) "
            + "ORDER BY update_status DESC",
            nativeQuery = true)
    List<Certificate> findAllForDownload(@Param("email") String email);

    List<Certificate> findAllBySendToEmailAndUpdateStatusAndSendStatusTrue(String sendToEmail, LocalDate updateDate);

    Optional<Certificate> findById(Long id);

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
