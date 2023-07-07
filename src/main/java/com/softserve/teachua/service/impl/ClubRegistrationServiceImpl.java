package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club_registration.ClubRegistrationRequest;
import com.softserve.teachua.dto.club_registration.ClubRegistrationResponse;
import com.softserve.teachua.dto.club_registration.RegistrationApprovedSuccess;
import com.softserve.teachua.dto.club_registration.UnapprovedClubRegistration;
import com.softserve.teachua.dto.club_registration.UserClubRegistrationRequest;
import com.softserve.teachua.dto.club_registration.UserClubRegistrationResponse;
import com.softserve.teachua.model.Child;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.ClubRegistration;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ClubRegistrationRepository;
import com.softserve.teachua.service.ChildService;
import com.softserve.teachua.service.ClubRegistrationService;
import com.softserve.teachua.service.ClubService;
import java.util.List;
import java.util.function.Function;
import com.softserve.teachua.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubRegistrationServiceImpl implements ClubRegistrationService {
    private final ClubRegistrationRepository clubRegistrationRepository;
    private final ChildService childService;
    private final UserService userService;
    private final ClubService clubService;
    private final DtoConverter dtoConverter;

    @Override
    public List<ClubRegistrationResponse> create(ClubRegistrationRequest clubRegistrationRequest) {
        Club club = clubService.getClubById(clubRegistrationRequest.getClubId());

        List<ClubRegistration> registered = clubRegistrationRequest.getChildIds().stream()
                .map(createFunction(club, clubRegistrationRequest.getComment()))
                .toList();
        ClubRegistrationResponse crr = new ClubRegistrationResponse();

        return registered.stream()
                .map(cr -> dtoConverter.convertToDto(cr, crr))
                .toList();
    }

    @Override
    public UserClubRegistrationResponse create(UserClubRegistrationRequest userClubRegistrationRequest) {
        Club club = clubService.getClubById(userClubRegistrationRequest.getClubId());
        User user = userService.getUserById(userClubRegistrationRequest.getUserId());

        ClubRegistration cr = ClubRegistration.builder()
                .club(club)
                .comment(userClubRegistrationRequest.getComment())
                .user(user)
                .build();
        cr = clubRegistrationRepository.save(cr);

        return dtoConverter.convertToDto(cr, new UserClubRegistrationResponse());
    }

    @Override
    public List<UnapprovedClubRegistration> getAllUnapprovedByManagerId(Long managerId) {
        var unapprovedClubRegistrations = clubRegistrationRepository.findAllUnapprovedByManagerId(managerId);
        var dto = new UnapprovedClubRegistration();
        return unapprovedClubRegistrations.stream()
                .map(ucr -> dtoConverter.convertToDto(ucr, dto))
                .toList();
    }

    @Override
    @Transactional
    public RegistrationApprovedSuccess approve(Long clubRegistrationId) {
        clubRegistrationRepository.approveClubRegistration(clubRegistrationId);

        return new RegistrationApprovedSuccess(clubRegistrationId, true);
    }

    @NotNull
    private Function<Long, ClubRegistration> createFunction(Club club, String comment) {
        return childId -> {
            Child child = childService.getById(childId);
            ClubRegistration clubRegistration = ClubRegistration.builder()
                    .club(club)
                    .comment(comment)
                    .child(child)
                    .build();

            return clubRegistrationRepository.save(clubRegistration);
        };
    }
}
