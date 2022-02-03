package com.softserve.teachua.repository;

import com.softserve.teachua.model.Center;
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

    List<Club> findAllByOrderByIdAsc();

    boolean existsByName(String name);

    boolean existsById(Long id);

    @Query("SELECT DISTINCT club from Club AS club "
            + "LEFT JOIN club.user AS user "
            + "LEFT JOIN club.center AS center WHERE "
            + "user.id = :id AND "
            + "center IS NULL")
    List<Club> findAllByUserId(@Param("id") Long id);

    Page<Club> findAllByUserId(Long id, Pageable pageable);

    @Query("SELECT DISTINCT club from Club AS club "
            + "JOIN club.categories AS category "
            + "LEFT JOIN club.locations AS locations "
            + "LEFT JOIN locations.city AS city "
            + "LEFT JOIN locations.district AS district "
            + "LEFT JOIN locations.station AS station WHERE "
            + "((:city NOT LIKE 'online' AND (:isOnline IS NULL OR club.isOnline = :isOnline) AND city.name = :city) OR"
            + "(:city LIKE 'online' AND club.isOnline = true) OR "
            + "(:city IS NULL OR city.name = :city)) AND "
            + "(:isOnline is null or club.isOnline=:isOnline) AND "
            + "(:age IS NULL or club.ageFrom <= :age AND club.ageTo >= :age) AND "
            + "((:categories) IS NULL OR category.name IN (:categories)) AND "
            + "(:district IS NULL OR district.name = :district) AND "
            + "(:name IS NULL OR "
            + "LOWER(club.name) LIKE LOWER(CONCAT('%', :name , '%')) OR "
            + "LOWER(club.description) LIKE LOWER(CONCAT('%', :name , '%'))) AND "
            + "(:station IS NULL OR station.name = :station)")
    Page<Club> findAllBylAdvancedSearch(
            @Param("name") String name,
            @Param("age") Integer age,
            @Param("city") String cityName,
            @Param("district") String districtName,
            @Param("station") String stationName,
            @Param("categories") List<String> categoriesName,
            @Param("isOnline") Boolean isOnline,
            Pageable pageable);

    @Query(value = "SELECT DISTINCT club from Club AS club "
            + "LEFT JOIN club.locations AS locations "
            + "LEFT JOIN locations.city AS city "
            + "JOIN club.categories AS category WHERE "
            + "(:name IS NULL OR "
            + "LOWER(club.name) LIKE LOWER(CONCAT('%', :name , '%')) OR "
            + "LOWER(club.description) LIKE LOWER(CONCAT('%', :name , '%'))) AND "
            + "(((:isOnline = false OR :isOnline IS NULL) AND city.name = :city ) OR "
            + "(:isOnline = true AND club.isOnline = true AND city IS NULL) OR "
            + "(:isOnline IS NULL AND :city IS NULL)) AND "
            + "(:category IS NULL OR LOWER(category.name) LIKE LOWER(CONCAT('%', :category ,'%')))")
    Page<Club> findAllByParameters(
            @Param("name") String name,
            @Param("city") String cityName,
            @Param("category") String categoryName,
            @Param("isOnline") Boolean isOnline,
            Pageable pageable);

    @Query(value = "SELECT DISTINCT club from Club AS club "
            + "JOIN club.locations AS locations "
            + "JOIN club.categories AS category WHERE "
            + "LOWER(category.name) LIKE LOWER(CONCAT('%', :category ,'%')) AND "
            + "locations.city.name = :city")
    Page<Club> findAllByCategoryNameAndCity(@Param("category") String categoryName,
                                            @Param("city") String cityName,
                                            Pageable pageable);

    @Query("SELECT DISTINCT club from Club AS club "
            + "JOIN club.locations AS locations "
            + "JOIN club.categories AS category WHERE "
            + "locations.city.name LIKE CONCAT('%', :city , '%') AND "
            + "LOWER(category.name) LIKE LOWER(CONCAT('%', :category ,'%'))")
    List<Club> findAllClubsByParameters(
            @Param("city") String cityName,
            @Param("category") String categoryName);


    @Query("SELECT c FROM Club AS c "
            + "JOIN c.locations AS locations "
            + "WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :text ,'%')) AND "
            + "locations.city.name = :city")
    Page<Club> findTop3ByName(@Param("text") String text,
                              @Param("city") String cityName,
                              Pageable pageable);

    /**
     * @author Vasyl Khula
     */
    @Query("select center.clubs from Center as center "
            + "join center.locations AS locations "
            + " where (lower (center.name)) LIKE lower (concat('%', :centerName , '%')) or "
            + " lower (center.description) LIKE lower (concat('%', :centerName , '%'))  and "
            + " locations.city.name = :cityName")
    Page<Club> findClubsByCenterName(@Param("centerName") String centerName,
                                     @Param("cityName") String cityName, Pageable pageable);


    @Query("SELECT DISTINCT club from Club AS club "
            + "JOIN club.locations AS locations "
            + "JOIN club.categories AS category WHERE "
            + "category.name IN (:categoriesName) AND "
            + "locations.city.name = :cityName "
            + "AND club.id <> :id")
    Page<Club> findByCategoriesNames(@Param("id") Long id,
                                     @Param("categoriesName") List<String> categoriesName,
                                     @Param("cityName") String cityName,
                                     Pageable pageable);

    @Modifying
    @Query(value = "UPDATE clubs SET rating=:rating, feedback_count = :feedback_count WHERE id = :club_id",
            nativeQuery = true)
    void updateRating(
            @Param("club_id") Long club_id,
            @Param("rating") double rating,
            @Param("feedback_count") Long feedback_count
    );

    List<Club> findClubByClubExternalId(Long id);

    List<Club> findClubsByCenter(Center center);

    @Query("SELECT case  when (AVG(club.rating)) is null then 0.0 else AVG(club.rating)  end FROM Club AS club"
            + " WHERE club.center.id = :centerId and club.feedbackCount > 0")
    Double findAvgRating(@Param("centerId") Long centerId);
}
