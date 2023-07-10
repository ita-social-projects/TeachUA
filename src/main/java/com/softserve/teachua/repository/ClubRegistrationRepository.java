package com.softserve.teachua.repository;

import com.softserve.teachua.model.ClubRegistration;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubRegistrationRepository extends JpaRepository<ClubRegistration, Long> {
    @Query("""
            SELECT cr
            FROM ClubRegistration cr
            WHERE cr.isApproved = false AND cr.club.user.id = :managerId AND cr.isActive = true
            ORDER BY cr.registrationDate ASC""")
    List<ClubRegistration> findAllUnapprovedByManagerIdOrderByRegistrationDateAsc(@Param("managerId") Long managerId);

    @Modifying
    @Query("""
            UPDATE ClubRegistration cr
            SET cr.isApproved = true
            WHERE cr.id = :clubRegistrationId""")
    void approveClubRegistration(@Param("clubRegistrationId") Long clubRegistrationId);

    @Query("""
            SELECT COUNT(cr) > 0
            FROM ClubRegistration cr
            WHERE cr.club.id = :clubId AND cr.child.id = :childId AND cr.isActive = true""")
    boolean existsActiveRegistration(@Param("clubId") Long clubId, @Param("childId") Long childId);
}
