package com.softserve.teachua.repository;

import com.softserve.teachua.model.CertificateDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateDatesRepository extends JpaRepository<CertificateDates, Integer> {

    List<CertificateDates> findAll();

    Optional<CertificateDates> findById(Integer integer);

    Optional<CertificateDates> findByDuration(String duration);

    boolean existsByDurationAndAndDate(String duration, String date);

    Optional<CertificateDates> findByDurationAndDate(String duration, String date);

    Optional<CertificateDates> findByHoursAndDate(Integer hours, String date);

    boolean existsByDate(String date);

    boolean existsByHoursAndDate(Integer hours, String date);
    //boolean existsByHoursAndAndDate(Integer hours, String date);

    Optional<CertificateDates> findByDate(String date);
}
