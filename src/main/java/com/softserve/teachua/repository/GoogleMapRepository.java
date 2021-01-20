package com.softserve.teachua.repository;

import com.softserve.teachua.entity.Coordinates;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleMapRepository extends JpaRepository<Coordinates, Integer> {

    @Query("select crd from Coordinates crd where crd.childrenCenter.address like:centerAddress")
    Coordinates getClubCoordinatesByAddress(String centerAddress);
}
