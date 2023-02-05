package com.softserve.teachua.repository;

import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findLocationsByCenter(Center center);

    Set<Location> deleteAllByClub(Club club);

    Set<Location> deleteAllByCenter(Center center);

    Optional<Location> findById(Long id);

    List<Location> findAll();
}
