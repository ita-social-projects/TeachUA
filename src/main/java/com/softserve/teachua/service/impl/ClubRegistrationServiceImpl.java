package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.dto.club_registration.ClubRegistrationRequest;
import com.softserve.teachua.dto.club_registration.ClubRegistrationResponse;
import com.softserve.teachua.dto.club_registration.RegistrationApprovedSuccess;
import com.softserve.teachua.dto.club_registration.RegistrationCanceledSuccess;
import com.softserve.teachua.dto.club_registration.UnapprovedClubRegistration;
import com.softserve.teachua.dto.club_registration.FullClubRegistration;
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
import java.util.Set;
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
        var unapprovedClubRegistrations = clubRegistrationRepository
                .findAllUnapprovedByManagerIdOrderByRegistrationDateAsc(managerId);
        var dto = new UnapprovedClubRegistration();

        return unapprovedClubRegistrations.stream()
                .map(ucr -> dtoConverter.convertToDto(ucr, dto))
                .toList();
    }

    @Override
    public List<FullClubRegistration> getAllByManagerId(Long managerId) {
        var clubRegistrations = clubRegistrationRepository
                .findAllByClubUserIdOrderByRegistrationDateAsc(managerId);
        var dto = new FullClubRegistration();

        return clubRegistrations.stream()
                .map(cr -> dtoConverter.convertToDto(cr, dto))
                .toList();
    }

    @Override
    @Transactional
    public RegistrationApprovedSuccess approve(Long clubRegistrationId) {
        clubRegistrationRepository.approveClubRegistration(clubRegistrationId);

        return new RegistrationApprovedSuccess(clubRegistrationId, true);
    }

    @Override
    @Transactional
    public RegistrationCanceledSuccess cancel(Long clubRegistrationId) {
        clubRegistrationRepository.cancelClubRegistration(clubRegistrationId);

        return new RegistrationCanceledSuccess(clubRegistrationId, false);
    }

    @Override
    public boolean existsActiveRegistration(Long clubId, Long childId) {
        return clubRegistrationRepository.existsActiveRegistration(clubId, childId);
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

    @Override
    public List<ChildResponse> getChildrenForCurrentUserAndCheckIsDisabledByClubId(Long clubId) {
        User user = userService.getAuthenticatedUserWithChildren();
        Set<Child> children = user.getChildren();
        ChildResponse cr = new ChildResponse();

        return children.stream()
                .map(c -> createChildResponseWithRegistrationCheck(clubId, cr, c))
                .toList();
    }

    @Override
    public List<FullClubRegistration> getApplicationsByUserId(Long userId) {
        FullClubRegistration ua = new FullClubRegistration();

        return clubRegistrationRepository.findRegistrationsByUserIdOrChildParentId(userId)
                .stream()
                .map(cr -> dtoConverter.convertToDto(cr, ua))
                .toList();
    }

    private ChildResponse createChildResponseWithRegistrationCheck(
            Long clubId, ChildResponse cr, Child c) {
        ChildResponse childResponse = dtoConverter.convertToDto(c, cr);
        if (existsActiveRegistration(clubId, c.getId())) {
            childResponse.setDisabled(true);
        }
        return childResponse;
    }
}
