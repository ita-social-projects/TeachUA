package com.softserve.teachua.service.impl;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.exception.NotVerifiedUserException;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.FeedbackRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ClubService;
import com.softserve.teachua.service.FeedbackService;
import com.softserve.teachua.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
    private static final String FEEDBACK_NOT_FOUND_BY_ID = "Feedback not found by id: %s";
    private static final String FEEDBACK_DELETING_ERROR = "Can't delete feedback cause of relationship";
    private static final String ACCESS_TO_FEEDBACK_DENIED = "User can edit/delete only own feedbacks";

    private final FeedbackRepository feedbackRepository;
    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private  final UserRepository userRepository;
    private final UserService userService;

  @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, DtoConverter dtoConverter, ClubRepository clubRepository, ArchiveService archiveService, UserRepository userRepository, UserService userService) {
        this.feedbackRepository = feedbackRepository;
        this.dtoConverter = dtoConverter;
        this.clubRepository = clubRepository;
        this.archiveService = archiveService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Method find {@link Feedback}, and convert it to object of DTO class
     *
     * @param id - place id here.
     * @return new {@code FeedbackResponse}
     **/
    @Override
    public FeedbackResponse getFeedbackProfileById(Long id) {
        return dtoConverter.convertToDto(getFeedbackById(id), FeedbackResponse.class);
    }

    /**
     * Method find {@link Feedback}
     *
     * @param id - place id
     * @return Feedback
     **/
    @Override
    public Feedback getFeedbackById(Long id) {
        Optional<Feedback> optionalFeedback = getOptionalFeedbackById(id);
        if (!optionalFeedback.isPresent()) {
            throw new NotExistException(String.format(FEEDBACK_NOT_FOUND_BY_ID, id));
        }

        Feedback feedback = optionalFeedback.get();
        log.debug("get feedback by id - " + feedback);
        return feedback;
    }

    @Override
    public List<FeedbackResponse> getAllByClubId(Long id) {
        List<FeedbackResponse> feedbackResponses = feedbackRepository.getAllByClubId(id)
                .stream()
                .map(feedback -> (FeedbackResponse) dtoConverter.convertToDto(feedback, FeedbackResponse.class))
                .collect(Collectors.toList());

        log.debug("get list of feedback -" + feedbackResponses);
        return feedbackResponses;
    }

    /**
     * Method add and save new {@link Feedback}
     *
     * @param feedbackProfile - put dto 'FeedbackProfile'
     * @return SuccessCreatedFeedback
     **/
    @Override
    public SuccessCreatedFeedback addFeedback(FeedbackProfile feedbackProfile, HttpServletRequest httpServletRequest) {

        feedbackProfile.setUserId(userService.getUserFromRequest(httpServletRequest).getId());

        if(!clubRepository.existsById(feedbackProfile.getClubId())){
            throw new NotExistException("Club with id "+feedbackProfile.getClubId()+" does`nt exists");
        }
        if(!userRepository.existsById(feedbackProfile.getUserId())){
            throw new NotExistException("User with id "+feedbackProfile.getUserId()+" does`nt exists");
        }
        Feedback feedback = feedbackRepository.save(dtoConverter.convertToEntity(feedbackProfile, new Feedback()));
//        Long clubId = feedback.getClub().getId();
        clubService.updateRatingNewFeedback(dtoConverter.convertToDto(feedback, FeedbackResponse.class));
//        clubRepository.updateRating(clubId, feedbackRepository.findAvgRating(clubId));
        log.debug("add new feedback - " + feedback);
        return dtoConverter.convertToDto(feedback, SuccessCreatedFeedback.class);
    }

    /**
     * Method find all {@link Feedback}
     *
     * @return new {@code List<FeedbackResponse>}
     **/
    @Override
    public List<FeedbackResponse> getListOfFeedback() {
        List<FeedbackResponse> feedbackResponses = feedbackRepository.findAll()
                .stream()
                .map(feedback -> (FeedbackResponse) dtoConverter.convertToDto(feedback, FeedbackResponse.class))
                .collect(Collectors.toList());

        log.debug("get list of feedback -" + feedbackResponses);
        return feedbackResponses;
    }

    /**
     * Method delete {@link Feedback} and update Club rating
     *
     * @param id - place id
     * @return new {@code FeedbackResponse}
     **/
    @Override
    public FeedbackResponse deleteFeedbackById(Long id) {
        Feedback feedback = getFeedbackById(id);


        archiveService.saveModel(feedback);

        try {
            feedbackRepository.deleteById(id);
            feedbackRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(FEEDBACK_DELETING_ERROR);
        }

        FeedbackResponse feedbackResponse = dtoConverter.convertToDto(feedback, FeedbackResponse.class);

        clubService.updateRatingDeleteFeedback(feedbackResponse);

        log.debug("feedback {} was successfully deleted", feedback);
        return feedbackResponse;
    }

    private Optional<Feedback> getOptionalFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    /**
     * Method find {@link Feedback} by id, and update data and Club rating
     *
     * @param id              - place id
     * @param feedbackProfile - put dto 'FeedbackProfile'
     * @return FeedbackResponse
     **/
    @Override
    public FeedbackResponse updateFeedbackProfileById(
            Long id,
            FeedbackProfile feedbackProfile,
            HttpServletRequest httpServletRequest
    ){
        validateFeedbackOwner(id, httpServletRequest);
        Feedback feedback = getFeedbackById(id);
        Feedback updFeedback = dtoConverter.convertToEntity(feedbackProfile, feedback)
                .withId(id)
                .withClub(feedback.getClub())
                .withUser(feedback.getUser())
                .withDate(feedback.getDate());

        FeedbackResponse feedbackResponse = dtoConverter.convertToDto(feedback, FeedbackResponse.class);
        FeedbackResponse updFeedbackResponse =
                dtoConverter.convertToDto(feedbackRepository.save(updFeedback), FeedbackResponse.class);

        clubService.updateRatingEditFeedback(feedbackResponse, updFeedbackResponse);

        return updFeedbackResponse;
    }

    @Override
    public void validateFeedbackOwner(Long id, HttpServletRequest httpServletRequest){
        User userFromFeedback = getFeedbackById(id).getUser();
        User userFromRequest = userService.getUserFromRequest(httpServletRequest);

        if(userFromRequest != null && userFromRequest.getRole().getName().equals(RoleData.ADMIN.getDBRoleName())){
            return;
        }

        if(!(userFromFeedback != null && userFromRequest != null && userFromRequest.equals(userFromFeedback))) {
            throw new NotVerifiedUserException(ACCESS_TO_FEEDBACK_DENIED);
        }
    }

}
