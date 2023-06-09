package com.softserve.club.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.club.dto.feedback.FeedbackProfile;
import com.softserve.club.dto.feedback.FeedbackResponse;
import com.softserve.club.dto.feedback.SuccessCreatedFeedback;
import com.softserve.club.model.Feedback;
import com.softserve.club.repository.ClubRepository;
import com.softserve.club.repository.FeedbackRepository;
import com.softserve.club.service.ClubService;
import com.softserve.club.service.FeedbackService;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class FeedbackServiceImpl implements FeedbackService/*, ArchiveMark<Feedback> */ {
    private static final String FEEDBACK_NOT_FOUND_BY_ID = "Feedback not found by id: %s";
    private static final String FEEDBACK_DELETING_ERROR = "Can't delete feedback cause of relationship";
    private static final String ACCESS_TO_FEEDBACK_DENIED = "User can edit/delete only own feedbacks";

    private final FeedbackRepository feedbackRepository;
    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;
    private final ClubService clubService;
    private final ObjectMapper objectMapper;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, DtoConverter dtoConverter,
                               ClubRepository clubRepository, @Lazy ClubService clubService,
                               ObjectMapper objectMapper) {
        this.feedbackRepository = feedbackRepository;
        this.dtoConverter = dtoConverter;
        this.clubRepository = clubRepository;
        this.clubService = clubService;
        this.objectMapper = objectMapper;
    }

    @Override
    public FeedbackResponse getFeedbackProfileById(Long id) {
        return dtoConverter.convertToDto(getFeedbackById(id), FeedbackResponse.class);
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        Optional<Feedback> optionalFeedback = getOptionalFeedbackById(id);
        if (optionalFeedback.isEmpty()) {
            throw new NotExistException(String.format(FEEDBACK_NOT_FOUND_BY_ID, id));
        }

        Feedback feedback = optionalFeedback.get();
        log.debug("get feedback by id - " + feedback);
        return feedback;
    }

    @Override
    public List<FeedbackResponse> getAllByClubId(Long id) {
        List<FeedbackResponse> feedbackResponses = feedbackRepository.getAllByClubId(id).stream()
                .map(feedback -> (FeedbackResponse) dtoConverter.convertToDto(feedback, FeedbackResponse.class))
                .toList();

        log.debug("get list of feedback -" + feedbackResponses);
        return feedbackResponses;
    }

    @Override
    public SuccessCreatedFeedback addFeedback(FeedbackProfile feedbackProfile) {
        //todo
        //feedbackProfile.setUserId(customUserDetailsService.getUserPrincipal().getId());
        //
        //if (!clubRepository.existsById(feedbackProfile.getClubId())) {
        //    throw new NotExistException("Club with id " + feedbackProfile.getClubId() + " does`nt exists");
        //}
        //if (!userRepository.existsById(feedbackProfile.getUserId())) {
        //    throw new NotExistException("User with id " + feedbackProfile.getUserId() + " does`nt exists");
        //}
        //Feedback feedback = feedbackRepository.save(dtoConverter.convertToEntity(feedbackProfile, new Feedback()));
        //clubService.updateRatingNewFeedback(dtoConverter.convertToDto(feedback, FeedbackResponse.class));
        //log.debug("add new feedback - " + feedback);
        //return dtoConverter.convertToDto(feedback, SuccessCreatedFeedback.class);
        throw new NotImplementedException();
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

        //archiveModel(feedback);

        FeedbackResponse feedbackResponse = dtoConverter.convertToDto(feedback, FeedbackResponse.class);

        if (Optional.ofNullable(feedbackResponse.getClub()).isPresent()) {
            clubService.updateRatingDeleteFeedback(feedbackResponse);
        }

        log.debug("feedback {} was successfully deleted", feedback);
        return feedbackResponse;
    }

    private Optional<Feedback> getOptionalFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    @Override
    public FeedbackResponse updateFeedbackProfileById(Long id, FeedbackProfile feedbackProfile) {
        //todo
        //User user = userService.getAuthenticatedUser();
        //validateFeedbackOwner(id, user);
        //Feedback feedback = getFeedbackById(id);
        //Feedback updFeedback = dtoConverter.convertToEntity(feedbackProfile, feedback).withId(id)
        //        .withClub(feedback.getClub()).withUser(feedback.getUser()).withDate(feedback.getDate());
        //
        //FeedbackResponse feedbackResponse = dtoConverter.convertToDto(feedback, FeedbackResponse.class);
        //FeedbackResponse updFeedbackResponse = dtoConverter.convertToDto(feedbackRepository.save(updFeedback),
        //        FeedbackResponse.class);
        //
        //clubService.updateRatingEditFeedback(feedbackResponse, updFeedbackResponse);
        //
        //return updFeedbackResponse;
        throw new NotImplementedException();
    }

    //private void validateFeedbackOwner(Long id, User userFromRequest) {
    //    User userFromFeedback = getFeedbackById(id).getUser();
    //
    //    if (userFromRequest != null && userFromRequest.getRole().getName().equals(RoleData.ADMIN.getDBRoleName())) {
    //        return;
    //    }
    //
    //    if (!(userFromRequest != null && userFromRequest.equals(userFromFeedback))) {
    //        throw new NotVerifiedUserException(ACCESS_TO_FEEDBACK_DENIED);
    //    }
    //}

    //todo
    //@Override
    //public void archiveModel(Feedback feedback) {
    //    archiveService.saveModel(dtoConverter.convertToDto(feedback, FeedbackArch.class));
    //}
    //
    //@Override
    //public void restoreModel(String archiveObject) throws JsonProcessingException {
    //    FeedbackArch feedbackArch = objectMapper.readValue(archiveObject, FeedbackArch.class);
    //    Feedback feedback = Feedback.builder().build();
    //    feedback = dtoConverter.convertToEntity(feedbackArch, feedback).withId(null);
    //    if (Optional.ofNullable(feedbackArch.getClubId()).isPresent()) {
    //        feedback.setClub(clubService.getClubById(feedbackArch.getClubId()));
    //    }
    //    if (Optional.ofNullable(feedbackArch.getUserId()).isPresent()) {
    //        feedback.setUser(userService.getUserById(feedbackArch.getUserId()));
    //    }
    //
    //    feedbackRepository.save(feedback);
    //}
}
