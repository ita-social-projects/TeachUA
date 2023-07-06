package com.softserve.teachua.service;

import com.softserve.teachua.dto.club_registration.RegistrationApprovedSuccess;
import com.softserve.teachua.dto.club_registration.UnapprovedClubRegistration;
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
}
