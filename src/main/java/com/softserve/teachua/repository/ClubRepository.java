package com.softserve.teachua.repository;

import com.softserve.teachua.model.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Club getById(Long id);

    Club findByName(String name);

    boolean existsByName(String name);

    boolean existsById(Long id);

    @Query("SELECT DISTINCT club from Club AS club " +
            "LEFT JOIN club.categories AS category WHERE " +
            "club.name LIKE CONCAT('%', :name ,'%') AND " +
            "club.city.name LIKE CONCAT('%', :city ,'%') AND " +
            "(category.name LIKE CONCAT('%', :category ,'%') " +
            "OR category.name IS NULL AND :category = '')")
    Page<Club> findAllByParameters(
            @Param("name") String name,
            @Param("city") String cityName,
            @Param("category") String categoryName,
            Pageable pageable);

    @Query(value = "SELECT *  FROM clubs AS c WHERE LOWER(c.name) LIKE LOWER('%' || :text || '%') ORDER BY RANDOM() LIMIT 3",
            nativeQuery=true)
    List<Club> findRandomTop3ByName(@Param("text") String enteredText);
}
