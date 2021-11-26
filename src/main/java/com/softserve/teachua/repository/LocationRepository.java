package com.softserve.teachua.repository;

import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findLocationsByCenter(Center center);

    Set<Location> deleteAllByClub(Club club);

    Set<Location> deleteAllByCenter(Center center);
}
