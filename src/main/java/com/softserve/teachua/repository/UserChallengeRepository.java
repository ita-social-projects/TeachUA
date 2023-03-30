package com.softserve.teachua.repository;

import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGet;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetChallengeDuration;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminNotRegisteredUser;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminRegisteredUser;
import com.softserve.teachua.dto.user_challenge.exist.UserChallengeForExist;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileGet;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetLocalDate;
import com.softserve.teachua.model.UserChallenge;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    @Query(value = "SELECT new com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileGet("
            + "u.id, "
            + "u.challengeDuration.challenge.id, "
            + "u.challengeDuration.challenge.name, "
            + "u.registrationDate, "
            + "u.challengeDuration.durationEntity.startDate, "
            + "u.challengeDuration.durationEntity.endDate, "
            + "u.userChallengeStatus.statusTitle) "
            + "FROM UserChallenge u "
            + "WHERE u.user.id = :id")
    List<UserChallengeForProfileGet> getUserChallengeForProfileByUserId(Long id);

    @Query("select distinct uc "
            + "from UserChallenge uc "
            + "where uc.user.id =?1 "
            + "and uc.challengeDuration.challenge.id =?2 "
            + "and uc.challengeDuration.durationEntity.startDate =?3 "
            + "and uc.challengeDuration.durationEntity.endDate =?4")
    UserChallenge getUserChallengeByUserIdChallengeIdStartDateEndDate(Long userId,
                                                                      Long challengeId,
                                                                      LocalDate startChallengeDate,
                                                                      LocalDate endChallengeDate);

    @Query("select distinct new com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetLocalDate("
            + "cd.durationEntity.startDate , "
            + "cd.durationEntity.endDate) "
            + "from ChallengeDuration cd "
            + "where cd.challenge.id =:id")
    List<UserChallengeForUserGetLocalDate> getListUserChallengeDurationByChallengeId(Long id);

    @Query(value = "select distinct new com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGet("
            + "    c.id,"
            + "    c.name,"
            + "    c.isActive)"
            + "from Challenge c")
    List<UserChallengeForAdminGet> getListUserChallengeForAdmin();

    UserChallenge getUserChallengeById(Long id);

    @Query(value = "select distinct "
              + "   new com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetChallengeDuration("
              + "   cd.challenge.id,"
              + "   cd.durationEntity.id,"
              + "   cd.userExist,"
              + "   cd.durationEntity.startDate,"
              + "   cd.durationEntity.endDate)"
              + "   from ChallengeDuration cd "
              + "   where cd.challenge.id =:id")
    List<UserChallengeForAdminGetChallengeDuration> getListChallengeDurationForAdmin(Long id);

    @Query(value = "SELECT new com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminRegisteredUser("
            + "uc.id, "
            + "uc.user.id, "
            + "uc.user.firstName,"
            + "uc.user.lastName, "
            + "uc.user.email,"
            + "uc.user.phone,"
            + "uc.user.role.name,"
            + "uc.userChallengeStatus.statusTitle,"
            + "uc.registrationDate)"
            + "FROM UserChallenge uc "
            + "WHERE uc.challengeDuration.challenge.id =?1 "
            + "and uc.challengeDuration.durationEntity.id =?2")
    List<UserChallengeForAdminRegisteredUser> getListRegisteredUsersByChallengeIdChallengeDurationId(Long challengeId,
                                                                                                     Long durationId);

    @Query(value = "select uc "
            + "           from UserChallenge uc"
            + "           where uc.challengeDuration.challenge.id =?1 "
            + "           and uc.challengeDuration.durationEntity.startDate =?2 "
            + "           and uc.challengeDuration.durationEntity.endDate =?3")
    List<UserChallenge> getListRegisteredByChallengeIdAndDates(Long challengeId, LocalDate startDate,
                                                               LocalDate endDate);

    @Query(value = "select  new com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminNotRegisteredUser("
            + "            u.id,"
            + "            u.firstName, "
            + "            u.lastName, "
            + "            u.email, "
            + "            u.phone,"
            + "            u.role.name )"
            + "            from User u "
            + "            where u.id <> (SELECT uc.user.id "
            + "                           from UserChallenge uc "
            + "                           where uc.challengeDuration.challenge.id =?1 "
            + "                           and uc.challengeDuration.durationEntity.id =?2 )")
    List<UserChallengeForAdminNotRegisteredUser> getListNotRegisteredUsersByChallengeIdChallengeDurationId(
            Long challengeId, Long durationId);

    @Query(value = "select new com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminNotRegisteredUser("
            + "u.id,"
            + "u.firstName, "
            + "u.lastName, "
            + "u.email, "
            + "u.phone, "
            + "u.role.name )"
            + "from User u ")
    List<UserChallengeForAdminNotRegisteredUser> getListAllNotRegisteredUsers();

    @Query("select distinct uc "
            + "from UserChallenge uc "
            + "where uc.user.id =?1 "
            + "and uc.challengeDuration.challenge.id =?2 "
            + "and uc.challengeDuration.durationEntity.id =?3 ")
    UserChallenge getUserChallengeByUserIdAndChallengeDurationId(Long userId, Long challengeId, Long durationId);

    @Query("select distinct new com.softserve.teachua.dto.user_challenge.exist.UserChallengeForExist("
            + "uc.user.id, "
            + "uc.challengeDuration.id)"
            + "from UserChallenge uc ")
    List<UserChallengeForExist> getListUserChallengeForExist();
}
