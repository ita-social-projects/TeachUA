package com.softserve.teachua.repository;

import com.softserve.teachua.model.GalleryPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryPhoto, Long> {

    List<GalleryPhoto> findAllByClubId(@Param("club_id") Long id);

    Long deleteAllByClub_Id(@Param("club_id") Long id);
}
