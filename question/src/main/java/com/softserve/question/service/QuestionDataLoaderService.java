package com.softserve.question.service;

import com.softserve.question.dto.question.QuestionDatabaseResponse;
import com.softserve.question.dto.question.question_excel.QuestionDataRequest;
import java.util.List;

/**
 * This interface contains all needed methods to manage questions data loader.
 */
public interface QuestionDataLoaderService {
    /**
     * This method saves dto {@code QuestionDatabaseResponse} to database, returns list of dto
     * {@code List<QuestionDatabaseResponse>} of messages.
     *
     * @param data - dto read from excel-file and form on page to save
     * @return new {@code List<QuestionDatabaseResponse>}
     */
    List<QuestionDatabaseResponse> saveToDatabase(QuestionDataRequest data, Long creatorI);
}
