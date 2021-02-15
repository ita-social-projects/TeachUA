package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.FeedbackRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final FeedbackRepository feedbackRepository;
    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, DtoConverter dtoConverter, ClubRepository clubRepository, ArchiveService archiveService) {
        this.feedbackRepository = feedbackRepository;
        this.dtoConverter = dtoConverter;
        this.clubRepository = clubRepository;
        this.archiveService = archiveService;
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
        log.info("get feedback by id - " + feedback);
        return feedback;
    }

    @Override
    public List<FeedbackResponse> getAllByClubId(Long id) {
        List<FeedbackResponse> feedbackResponses = feedbackRepository.getAllByClubId(id)
                .stream()
                .map(feedback -> (FeedbackResponse) dtoConverter.convertToDto(feedback, FeedbackResponse.class))
                .collect(Collectors.toList());

        log.info("get list of feedback -" + feedbackResponses);
        return feedbackResponses;
    }

    /**
     * Method add and save new {@link Feedback}
     *
     * @param feedbackProfile - put dto 'FeedbackProfile'
     * @return SuccessCreatedFeedback
     **/
    @Override
    public SuccessCreatedFeedback addFeedback(FeedbackProfile feedbackProfile) {
        Feedback feedback = feedbackRepository.save(dtoConverter.convertToEntity(feedbackProfile, new Feedback()));

        Long clubId = feedback.getClub().getId();

        clubRepository.updateRating(clubId, feedbackRepository.findAvgRating(clubId));

        log.info("add new feedback - " + feedback);
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

        log.info("get list of feedback -" + feedbackResponses);
        return feedbackResponses;
    }

    /**
     * Method find {@link Feedback} by id, and update data and Club rating
     *
     * @param id - place id
     * @param feedbackProfile  - put dto 'FeedbackProfile'
     * @return FeedbackProfile
     **/
    @Override
    public FeedbackProfile updateFeedbackProfileById(Long id, FeedbackProfile feedbackProfile) {
        Feedback feedback = getFeedbackById(id);

        Feedback newFeedback = dtoConverter.convertToEntity(feedbackProfile, feedback)
                .withId(id);

        feedbackRepository.save(newFeedback);
        clubRepository.updateRating(feedbackProfile.getClubId(), feedbackRepository.findAvgRating(feedbackProfile.getClubId()));

        log.info("updated feedback " + newFeedback);
        return dtoConverter.convertToDto(newFeedback, FeedbackProfile.class);
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

        Long clubId = feedback.getClub().getId();

        archiveService.saveModel(feedback);

        try {
            feedbackRepository.deleteById(id);
            feedbackRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(FEEDBACK_DELETING_ERROR);
        }

        clubRepository.updateRating(clubId, feedbackRepository.findAvgRating(clubId));

        log.info("feedback {} was successfully deleted", feedback);
        return dtoConverter.convertToDto(feedback, FeedbackResponse.class);
    }

    private Optional<Feedback> getOptionalFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }
}
