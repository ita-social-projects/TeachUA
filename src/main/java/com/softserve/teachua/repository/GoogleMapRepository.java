package com.softserve.teachua.repository;

import com.softserve.teachua.entity.Coordinates;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleMapRepository extends JpaRepository<Coordinates, Integer> {

    @Query(value = "select * from club_coordinates as crd join children_center as cc on cc.coordinates_id=crd.id where cc.address=:centerAddress", nativeQuery = true)
    Coordinates getClubCoordinatesByAddress(@Param("centerAddress") String centerAddress);
}
