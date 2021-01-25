package com.softserve.teachua.repository;

import com.softserve.teachua.model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioRepository extends JpaRepository <Studio, Long> {

    Studio getById(Long id);
}
