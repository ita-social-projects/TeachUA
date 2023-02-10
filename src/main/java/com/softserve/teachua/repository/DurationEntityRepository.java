package com.softserve.teachua.repository;

import com.softserve.teachua.model.DurationEntity;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DurationEntityRepository extends JpaRepository<DurationEntity, Long> {
    boolean existsDurationEntityByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}
