package com.softserve.teachua.repository;

import com.softserve.teachua.model.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findById(Long id);

    Optional<Club> findByName(String name);

    boolean existsByName(String name);

    boolean existsById(Long id);

    @Query("SELECT DISTINCT club from Club AS club " +
            "JOIN club.categories AS category WHERE " +
            "LOWER(club.name) LIKE LOWER(CONCAT('%', :name , '%')) AND " +
            "club.city.name LIKE CONCAT('%', :city , '%') AND " +
            "club.district.name LIKE CONCAT('%', :district , '%') AND " +
            "club.station.name LIKE CONCAT('%', :station , '%') AND " +
            "category.name LIKE CONCAT('%', :category ,'%')")
    Page<Club> findAllByParameters(
            @Param("name") String name,
            @Param("city") String cityName,
            @Param("district") String districtName,
            @Param("station") String stationName,
            @Param("category") String categoryName,
            Pageable pageable);

    @Query(value =
            "SELECT c FROM Club AS c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :text ,'%')) AND " +
            "c.city.name = :city")
    Page<Club> findTop3ByName(@Param("text") String text,
                              @Param("city") String cityName,
                              Pageable pageable);

    @Query("SELECT DISTINCT club from Club AS club " +
            "JOIN club.categories AS category WHERE " +
            "category.name = :categoryName " +
            "AND club.city.name = :cityName " +
            "AND club.id <> :id")
    Page<Club> findTop2ByCategoryName(@Param("id") Long id,
                                      @Param("categoryName") String categoryName,
                                      @Param("cityName") String cityName,
                                      Pageable pageable);

    @Modifying
    @Query(value = "UPDATE clubs SET rating=:rating WHERE id = :club_id", nativeQuery = true)
    void updateRating(@Param("club_id") Long club_id, @Param("rating") double rating);
}
