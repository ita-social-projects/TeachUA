package com.softserve.teachua.repository;

import com.softserve.teachua.entity.Club;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query("select c from Club c inner join ChildrenCenter  cc on c.childrenCenter.id = cc.id where cc.city.id = :id")
    List<Club> getClubByCityId(@Param("id") Long id);
}
