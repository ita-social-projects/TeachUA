package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.question.QuestionProfile;
import com.softserve.teachua.dto.question.QuestionResponse;
import com.softserve.teachua.model.Question;
import com.softserve.teachua.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController implements Api {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * The method to get Question by id
     *
     * @param id of Question
     * @return Question
     */
    @GetMapping("/question/{id}")
    public Question getNews(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    /**
     * The method to create a new Question
     *
     * @param questionProfile - object of DTO class
     * @return QuestionResponse
     */
    @PostMapping("/question")
    public QuestionResponse addNews(@RequestBody QuestionProfile questionProfile) {
        return questionService.addQuestion(questionProfile);
    }

    /**
     * The method to update Question
     *
     * @param id
     * @param questionProfile
     * @return QuestionProfile
     */
    @PutMapping("/question/{id}")
    public QuestionProfile updateNewsById(@PathVariable Long id, @RequestBody QuestionProfile questionProfile) {
        return questionService.updateQuestionById(id, questionProfile);
    }

    /**
     * The method to delete Question
     *
     * @param id
     * @return QuestionProfile
     */
    @DeleteMapping("/question/{id}")
    public QuestionProfile deleteNews(@PathVariable Long id) {
        return questionService.deleteQuestionById(id);
    }

    /**
     * The method to get all Questions
     *
     * @return List of QuestionResponse
     */
    @GetMapping("/questions")
    public List<QuestionResponse> getAllNews() {
        return questionService.getAllQuestions();
    }
}
