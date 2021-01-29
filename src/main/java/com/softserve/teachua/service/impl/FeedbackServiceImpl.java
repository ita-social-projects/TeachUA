package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.controller.CenterResponse;
import com.softserve.teachua.dto.controller.FeedbackResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedCenter;
import com.softserve.teachua.dto.controller.SuccessCreatedFeedback;
import com.softserve.teachua.dto.service.FeedbackProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.repository.FeedbackRepository;
import com.softserve.teachua.service.FeedbackService;
import javassist.NotFoundException;
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
    private final DtoConverter dtoConverter;

    @Autowired
    FeedbackServiceImpl(FeedbackRepository feedbackRepository, DtoConverter dtoConverter) {
        this.feedbackRepository = feedbackRepository;
        this.dtoConverter = dtoConverter;
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
        log.info("add new feedback - " + feedback);
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
}
