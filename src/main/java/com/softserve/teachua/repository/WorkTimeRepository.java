package com.softserve.teachua.repository;

import com.softserve.teachua.model.WorkTime;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Long> {
    void deleteById(@NotNull Long id);
}
