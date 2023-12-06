package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.SortAndPageDto;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.ReplyRequest;
import com.softserve.teachua.dto.feedback.ReplyResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.NotVerifiedUserException;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.FeedbackArch;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.FeedbackRepository;
import com.softserve.teachua.security.CustomUserDetailsService;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ClubService;
import com.softserve.teachua.service.CenterService;
import com.softserve.teachua.service.FeedbackService;
import com.softserve.teachua.service.UserService;
import java.util.List;
import java.util.Optional;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class FeedbackServiceImpl implements FeedbackService, ArchiveMark<Feedback> {
    private static final String FEEDBACK_NOT_FOUND_BY_ID = "Feedback not found by id: %s";
    private static final String FEEDBACK_DELETING_ERROR = "Can't delete feedback cause of relationship";
    private static final String ACCESS_TO_FEEDBACK_DENIED = "User can edit/delete only own feedbacks";

    private final FeedbackRepository feedbackRepository;
    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final UserService userService;
    private final ClubService clubService;
    private final CenterService centerService;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, DtoConverter dtoConverter,
                               ClubRepository clubRepository, ArchiveService archiveService,
                               UserService userService, @Lazy ClubService clubService,
                               @Lazy CenterService centerService, ObjectMapper objectMapper,
                               CustomUserDetailsService customUserDetailsService) {
        this.feedbackRepository = feedbackRepository;
        this.dtoConverter = dtoConverter;
        this.clubRepository = clubRepository;
        this.archiveService = archiveService;
        this.userService = userService;
        this.clubService = clubService;
        this.centerService = centerService;
        this.objectMapper = objectMapper;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public FeedbackResponse getFeedbackProfileById(Long id) {
        return dtoConverter.convertToDto(getFeedbackById(id), FeedbackResponse.class);
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(FEEDBACK_NOT_FOUND_BY_ID, id)));

        log.debug("get feedback by id - " + feedback);
        return feedback;
    }

    @Override
    public List<FeedbackResponse> getAllByClubId(Long id) {
        FeedbackResponse fr = new FeedbackResponse();
        List<FeedbackResponse> feedbackResponses = feedbackRepository
                .getAllByClubIdAndParentCommentIsNull(id).stream()
                .map(feedback -> dtoConverter.convertToDto(feedback, fr))
                .toList();

        log.debug("get list of feedback -" + feedbackResponses);
        return feedbackResponses;
    }

    @Override
    public Page<FeedbackResponse> getPageByClubId(Long id, SortAndPageDto sortAndPageDto) {
        Pageable pageable = PageRequest.of(sortAndPageDto.getPage(), sortAndPageDto.getSize());
        FeedbackResponse fr = new FeedbackResponse();

        Page<Feedback> feedbackResponses = feedbackRepository
                .getAllByClubIdAndParentCommentIsNullOrderByDateDesc(id, pageable);

        log.debug("get list of feedback {} \n Total pages {}", feedbackResponses,
                feedbackResponses.getTotalPages());
        return new PageImpl<>(
                feedbackResponses.stream()
                        .map(feedback -> dtoConverter.convertToDto(feedback, fr))
                        .toList(),
                feedbackResponses.getPageable(),
                feedbackResponses.getTotalElements()
        );
    }

    @Override
    public SuccessCreatedFeedback createFeedback(FeedbackProfile feedbackProfile) {
        feedbackProfile.setUserId(customUserDetailsService.getUserPrincipal().getId());

        if (!clubRepository.existsById(feedbackProfile.getClubId())) {
            throw new NotExistException("Club with id " + feedbackProfile.getClubId() + " does`nt exists");
        }

        User user = userService.getUserById(feedbackProfile.getUserId());
        Feedback feedback = feedbackRepository.save(dtoConverter.convertToEntity(feedbackProfile, new Feedback()));
        feedback.setUser(user);

        clubService.updateRatingNewFeedback(dtoConverter.convertToDto(feedback, FeedbackResponse.class));
        centerService.updateRatingNewFeedback(dtoConverter.convertToDto(feedback, FeedbackResponse.class));

        log.debug("add new feedback - " + feedback);
        log.debug("user = {}", feedback.getUser());
        return dtoConverter.convertToDto(feedback, SuccessCreatedFeedback.class);
    }

    @Override
    public List<FeedbackResponse> getListOfFeedback() {
        List<FeedbackResponse> feedbackResponses = feedbackRepository.findAll().stream()
                .map(feedback -> (FeedbackResponse) dtoConverter.convertToDto(feedback, FeedbackResponse.class))
                .toList();

        log.debug("get list of feedback -" + feedbackResponses);
        return feedbackResponses;
    }

    @Override
    public FeedbackResponse deleteFeedbackById(Long id) {
        Feedback feedback = getFeedbackById(id);

        try {
            feedbackRepository.deleteById(id);
            feedbackRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(FEEDBACK_DELETING_ERROR);
        }

        archiveModel(feedback);

        FeedbackResponse feedbackResponse = dtoConverter.convertToDto(feedback, FeedbackResponse.class);

        if (Optional.ofNullable(feedbackResponse.getClub()).isPresent()) {
            clubService.updateRatingDeleteFeedback(feedbackResponse);
        }

        log.debug("feedback {} was successfully deleted", feedback);
        return feedbackResponse;
    }

    @Override
    public FeedbackResponse updateFeedbackProfileById(Long id, FeedbackProfile feedbackProfile) {
        User user = userService.getAuthenticatedUser();
        validateFeedbackOwner(id, user);
        Feedback feedback = getFeedbackById(id);
        Feedback updFeedback = dtoConverter.convertToEntity(feedbackProfile, feedback).withId(id)
                .withClub(feedback.getClub()).withUser(feedback.getUser()).withDate(feedback.getDate());

        FeedbackResponse feedbackResponse = dtoConverter.convertToDto(feedback, FeedbackResponse.class);
        FeedbackResponse updFeedbackResponse = dtoConverter.convertToDto(feedbackRepository.save(updFeedback),
                FeedbackResponse.class);

        clubService.updateRatingEditFeedback(feedbackResponse, updFeedbackResponse);

        return updFeedbackResponse;
    }

    @Override
    public void validateFeedbackOwner(Long id, User userFromRequest) {
        User userFromFeedback = getFeedbackById(id).getUser();

        if (userFromRequest != null && userFromRequest.getRole().getName().equals(RoleData.ADMIN.getDBRoleName())) {
            return;
        }

        if (!(userFromRequest != null && userFromRequest.equals(userFromFeedback))) {
            throw new NotVerifiedUserException(ACCESS_TO_FEEDBACK_DENIED);
        }
    }

    @Override
    public ReplyResponse createReply(ReplyRequest replyRequest) {
        Feedback parentComment = getFeedbackById(replyRequest.getParentCommentId());
        Feedback reply = dtoConverter.convertToEntity(replyRequest, new Feedback());
        User user = userService.getUserById(replyRequest.getUserId());

        reply.setUser(user);
        parentComment.addReply(reply);

        Feedback savedReply = feedbackRepository.save(reply);
        log.debug("reply created {}", savedReply);

        return dtoConverter.convertToDto(savedReply, new ReplyResponse());
    }

    @Override
    public long countByClubId(Long clubId) {
        return feedbackRepository.countByClubIdAndParentCommentIsNull(clubId);
    }

    @Override
    public float ratingByClubId(Long clubId) {
        return feedbackRepository.findAverageRateByClubId(clubId).orElse(0f);
    }

    @Override
    public void archiveModel(Feedback feedback) {
        archiveService.saveModel(dtoConverter.convertToDto(feedback, FeedbackArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        FeedbackArch feedbackArch = objectMapper.readValue(archiveObject, FeedbackArch.class);
        Feedback feedback = Feedback.builder().build();
        feedback = dtoConverter.convertToEntity(feedbackArch, feedback).withId(null);
        if (Optional.ofNullable(feedbackArch.getClubId()).isPresent()) {
            feedback.setClub(clubService.getClubById(feedbackArch.getClubId()));
        }
        if (Optional.ofNullable(feedbackArch.getUserId()).isPresent()) {
            feedback.setUser(userService.getUserById(feedbackArch.getUserId()));
        }

        feedbackRepository.save(feedback);
    }
}
