package com.softserve.club.repository;

import com.softserve.club.model.GalleryPhoto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryPhoto, Long> {
    List<GalleryPhoto> findAllByClubId(@Param("club_id") Long id);

    Long deleteAllByClubId(@Param("club_id") Long id);
}
