package com.softserve.teachua.service;

import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.dto.club_registration.RegistrationApprovedSuccess;
import com.softserve.teachua.dto.club_registration.RegistrationCanceledSuccess;
import com.softserve.teachua.dto.club_registration.UnapprovedClubRegistration;
import com.softserve.teachua.dto.club_registration.FullClubRegistration;
import com.softserve.teachua.dto.club_registration.UserClubRegistrationRequest;
import com.softserve.teachua.dto.club_registration.ClubRegistrationRequest;
import com.softserve.teachua.dto.club_registration.ClubRegistrationResponse;
import com.softserve.teachua.dto.club_registration.UserClubRegistrationResponse;
import java.util.List;

public interface ClubRegistrationService {

    List<ClubRegistrationResponse> create(ClubRegistrationRequest clubRegistrationRequest);

    UserClubRegistrationResponse create(UserClubRegistrationRequest userClubRegistrationRequest);

    List<UnapprovedClubRegistration> getAllUnapprovedByManagerId(Long managerId);

    RegistrationApprovedSuccess approve(Long clubRegistrationId);

    boolean existsActiveRegistration(Long clubId, Long childId);

    List<ChildResponse> getChildrenForCurrentUserAndCheckIsDisabledByClubId(Long clubId);

    List<FullClubRegistration> getApplicationsByUserId(Long userId);

    RegistrationCanceledSuccess cancel(Long clubRegistrationId);

    List<FullClubRegistration> getAllByManagerId(Long managerId);
}
