package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.dto.feedback.SuccessCreatedFeedback;
import com.softserve.teachua.dto.feedback.FeedbackProfile;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.FeedbackRepository;
import com.softserve.teachua.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {
    private final String FEEDBACK_NOT_FOUND_BY_ID = "Feedback not found by id: %s";

    private boolean isFeedbackExistById(Long id) {
        return feedbackRepository.existsById(id);
    }

    private final FeedbackRepository feedbackRepository;
    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;

    @Autowired
    FeedbackServiceImpl(FeedbackRepository feedbackRepository, DtoConverter dtoConverter, ClubRepository clubRepository) {
        this.feedbackRepository = feedbackRepository;
        this.dtoConverter = dtoConverter;
        this.clubRepository = clubRepository;
    }

    /**
     * Method find {@link Feedback}, and convert it to object of DTO class
     *
     * @param id
     * @return FeedbackResponce
     **/
    @Override
    public FeedbackResponse getFeedbackProfileById(Long id) {
        return dtoConverter.convertToDto(getFeedbackById(id), FeedbackResponse.class);
    }

    /**
     * Method find {@link Feedback}
     *
     * @param id
     * @return Feedback
     **/
    @Override
    public Feedback getFeedbackById(Long id) {
        if (!isFeedbackExistById(id)) {
            String feedbackNotFoundById = String.format(FEEDBACK_NOT_FOUND_BY_ID, id);
            log.error(feedbackNotFoundById);
            throw new NotExistException(feedbackNotFoundById);
        }
        Feedback feedback = feedbackRepository.getById(id);
        log.info("get feedback by id - " + feedback);
        return feedback;
    }

    /**
     * Method add and save new {@link Feedback}
     *
     * @param feedbackProfile
     * @return SuccessCreatedFeedback
     **/
    @Override
    public SuccessCreatedFeedback addFeedback(FeedbackProfile feedbackProfile) {
        Feedback feedback = feedbackRepository.save(dtoConverter.convertToEntity(feedbackProfile, Feedback.builder().build()));
        clubRepository.updateRating(feedbackProfile.getClub().getId(),feedbackRepository.findAvgRating(feedbackProfile.getClub().getId()));
        log.info("add new feedback - "+ feedback);
        return dtoConverter.convertToDto(feedback, SuccessCreatedFeedback.class);
    }

    /**
     * Method find all {@link Feedback}
     *
     * @return List of FeedbackResponse
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
     * @param id
     * @param feedbackProfile
     * @return FeedbackProfile
     **/
    @Override
    public FeedbackProfile updateFeedbackProfileById(Long id,FeedbackProfile feedbackProfile) {
        if (!isFeedbackExistById(id)) {
            String feedbackNotFoundById = String.format(FEEDBACK_NOT_FOUND_BY_ID, id);
            log.error(feedbackNotFoundById);
            throw new NotExistException(feedbackNotFoundById);
        }
        Feedback feedback = dtoConverter.convertToEntity(feedbackProfile, new Feedback());
        feedback.setId(id);
        feedbackRepository.save(feedback);
        log.info("update feedback "+feedback);
        clubRepository.updateRating(feedbackProfile.getClub().getId(),feedbackRepository.findAvgRating(feedbackProfile.getClub().getId()));
        return feedbackProfile;
    }

    /**
     * Method delete {@link Feedback} and update Club rating
     *
     * @param id
     * @return FeedbackResponce
     **/
    @Override
    public FeedbackResponse deleteFeedbackById(Long id) {
        if (!isFeedbackExistById(id)) {
            String feedbackNotFoundById = String.format(FEEDBACK_NOT_FOUND_BY_ID, id);
            log.error(feedbackNotFoundById);
            throw new NotExistException(feedbackNotFoundById);
        }
        FeedbackResponse feedbackResponse = dtoConverter.convertToDto(feedbackRepository.getById(id), FeedbackResponse.class);
        feedbackRepository.deleteById(id);
        log.info("delete feedback "+feedbackResponse);
        if(feedbackRepository.findAvgRating(feedbackResponse.getClub().getId())==null){
        clubRepository.updateRating(feedbackResponse.getClub().getId(),0);
        }
        else{
            clubRepository.updateRating(feedbackResponse.getClub().getId(),
                    feedbackRepository.findAvgRating(feedbackResponse.getClub().getId()));
        }

        return feedbackResponse;
    }
}
