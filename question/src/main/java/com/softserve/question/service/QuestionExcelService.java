package com.softserve.question.service;

import com.softserve.question.dto.answer.answer_excel.AnswerExcel;
import com.softserve.question.dto.question.question_excel.ExcelQuestionParsingResponse;
import com.softserve.question.dto.question.question_excel.QuestionExcel;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.web.multipart.MultipartFile;

/**
 * This interface contains all needed methods to manage exel parser.
 */

public interface QuestionExcelService {
    /**
     * This method parses excel-file and returns {@code ExcelQuestionParsingResponse} of mistakes and created dto.
     *
     * @param multipartFile
     *            - put bode of excel-file to parse
     *
     * @return new {@code ExcelQuestionParsingResponse}.
     */
    ExcelQuestionParsingResponse parseExcel(MultipartFile multipartFile);

    byte[] exportToExcel();

    List<QuestionExcel> createQuestions(List<List<Cell>> rows);

    List<AnswerExcel> createAnswers(List<List<Cell>> rows);
}
