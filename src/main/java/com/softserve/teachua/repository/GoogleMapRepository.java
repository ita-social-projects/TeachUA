package com.softserve.teachua.repository;

import com.softserve.teachua.entity.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleMapRepository extends JpaRepository<Coordinates, Integer> {

    @Query("select crd from Coordinates crd join Club cl ON cl.coordinatesId=:clubId")
    Coordinates getClubCoordinatesById(Integer clubId);
}
