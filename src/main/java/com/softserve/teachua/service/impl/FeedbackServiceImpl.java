package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.controller.FeedbackResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedFeedback;
import com.softserve.teachua.dto.service.FeedbackProfile;
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

    @Override
    public FeedbackResponse getFeedbackProfileById(Long id) {
        return dtoConverter.convertToDto(getFeedbackById(id), FeedbackResponse.class);
    }

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

    @Override
    public SuccessCreatedFeedback addFeedback(FeedbackProfile feedbackProfile) {
        Feedback feedback = feedbackRepository.save(dtoConverter.convertToEntity(feedbackProfile, Feedback.builder().build()));
        clubRepository.updateRating(feedbackProfile.getClub().getId(),feedbackRepository.findAvgRating(feedbackProfile.getClub().getId()));
        log.info("add new feedback - "+ feedback);
        return dtoConverter.convertToDto(feedback, SuccessCreatedFeedback.class);
    }

    @Override
    public List<FeedbackResponse> getListOfFeedback() {
        List<FeedbackResponse> feedbackResponses = feedbackRepository.findAll()
                .stream()
                .map(feedback -> (FeedbackResponse) dtoConverter.convertToDto(feedback, FeedbackResponse.class))
                .collect(Collectors.toList());

        log.info("get list of feedback -" + feedbackResponses);
        return feedbackResponses;
    }

    @Override
    public FeedbackProfile updateFeedbackProfileById(Long id,FeedbackProfile feedbackProfile) {
        Feedback feedback = new Feedback().builder()
                .id(id)
                .userName(feedbackProfile.getUserName())
                .rate(feedbackProfile.getRate())
                .text(feedbackProfile.getText())
                .club(feedbackProfile.getClub())
                .build();
        feedbackRepository.save(feedback);
        clubRepository.updateRating(feedbackProfile.getClub().getId(),feedbackRepository.findAvgRating(feedbackProfile.getClub().getId()));
        return feedbackProfile;
    }

    @Override
    public FeedbackResponse deleteFeedbackById(Long id) {
        FeedbackResponse feedbackResponse = dtoConverter.convertToDto(feedbackRepository.getById(id), FeedbackResponse.class);
        feedbackRepository.deleteById(id);
        clubRepository.updateRating(feedbackResponse.getClub().getId(),feedbackRepository.findAvgRating(feedbackResponse.getClub().getId()));
        return feedbackResponse;
    }
}
