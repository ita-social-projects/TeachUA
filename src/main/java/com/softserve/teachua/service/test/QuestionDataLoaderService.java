package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.question.QuestionDatabaseResponse;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionDataRequest;
import java.util.List;

/**
 * This interface contains all needed methods to manage questions data loader.
 */
public interface QuestionDataLoaderService {
    /**
     * This method saves dto {@code QuestionDatabaseResponse} to database, returns list of dto
     * {@code List<QuestionDatabaseResponse>} of messages
     *
     * @param data - dto read from excel-file and form on page to save
     * @return new {@code List<QuestionDatabaseResponse>}
     */
    List<QuestionDatabaseResponse> saveToDatabase(QuestionDataRequest data, Long creatorI);
}
