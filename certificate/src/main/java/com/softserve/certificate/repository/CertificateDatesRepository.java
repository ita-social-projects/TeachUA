package com.softserve.certificate.repository;

import com.softserve.certificate.model.CertificateDates;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateDatesRepository extends JpaRepository<CertificateDates, Integer> {
    boolean existsByDateAndHoursAndDurationAndCourseNumberAndStudyForm(String date, Integer hours,
                                                                       String duration,
                                                                       String courseNumber,
                                                                       String studyForm);

    Optional<CertificateDates> findFirstByDateAndHoursAndDurationAndCourseNumberAndStudyForm(String date, Integer hours,
                                                                                             String duration,
                                                                                             String courseNumber,
                                                                                             String studyForm);
}
