package com.softserve.teachua.repository;

import com.softserve.teachua.model.ChallengeRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeRegistrationRepository extends JpaRepository<ChallengeRegistration, Long> {
    @Query("""
            SELECT cr
            FROM ChallengeRegistration cr
            WHERE cr.isApproved = false AND cr.challenge.user.id = :managerId AND cr.isActive = true
            ORDER BY cr.registrationDate ASC""")
    List<ChallengeRegistration> findAllUnapprovedByManagerId(@Param("managerId") Long managerId);

    List<ChallengeRegistration> findByChallenge_User_Id(Long id);

    @Modifying
    @Query("""
            UPDATE ChallengeRegistration cr
            SET cr.isApproved = true
            WHERE cr.id = :challengeRegistrationId""")
    void approveChallengeRegistration(@Param("challengeRegistrationId") Long challengeRegistrationId);

    @Modifying
    @Query("""
            UPDATE ChallengeRegistration cr
            SET cr.isActive = false
            WHERE cr.id = :challengeRegistrationId""")
    void cancelChallengeRegistration(@Param("challengeRegistrationId") Long challengeRegistrationId);

    @Query("""
            SELECT COUNT(cr) > 0
            FROM ChallengeRegistration cr
            WHERE cr.challenge.id = :challengeId AND cr.child.id = :childId AND cr.isActive = true""")
    boolean existsActiveRegistrationByChildIdAndChallengeId(@Param("challengeId") Long challengeId, @Param("childId") Long childId);
    boolean existsByChallenge_IdAndUser_Id(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    @Query("""
    SELECT cr
    FROM ChallengeRegistration cr
    LEFT JOIN cr.child c
    WHERE cr.user.id = :userId OR c.parent.id = :userId
    ORDER BY cr.registrationDate DESC""")
    List<ChallengeRegistration> findRegistrationsByUserIdOrChildParentId(@Param("userId") Long userId);
}
