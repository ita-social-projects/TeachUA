package com.softserve.teachua.repository;

import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
