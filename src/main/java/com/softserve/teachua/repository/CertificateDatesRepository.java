package com.softserve.teachua.repository;

import com.softserve.teachua.model.CertificateDates;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateDatesRepository extends JpaRepository<CertificateDates, Long> {
    List<CertificateDates> findAll();


    boolean existsByDateAndHoursAndDurationAndCourseNumberAndStudyForm(String date, Integer hours,
                                                                       String duration,
                                                                       String courseNumber,
                                                                       String studyForm);

    Optional<CertificateDates> findFirstByDateAndHoursAndDurationAndCourseNumberAndStudyForm(String date, Integer hours,
                                                                                             String duration,
                                                                                             String courseNumber,
                                                                                             String studyForm);
}
