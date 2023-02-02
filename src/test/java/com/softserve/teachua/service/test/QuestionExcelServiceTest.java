package com.softserve.teachua.service.test;

import static com.softserve.teachua.TestUtils.getAnswerExcel;
import static com.softserve.teachua.TestUtils.getEmptyCell;
import static com.softserve.teachua.TestUtils.getEmptySheet;
import static com.softserve.teachua.TestUtils.getIndexes;
import static com.softserve.teachua.TestUtils.getListOfEmptyCells;
import static com.softserve.teachua.TestUtils.getQuestionExcel;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import com.softserve.teachua.dto.test.answer.answerExcel.AnswerExcel;
import com.softserve.teachua.dto.test.question.questionExcel.ExcelQuestionParsingResponse;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionExcel;
import com.softserve.teachua.service.ExcelParserService;
import com.softserve.teachua.service.test.impl.QuestionExcelServiceImpl;
import com.softserve.teachua.constants.excel.ExcelColumn;
import com.softserve.teachua.constants.excel.QuestionExcelColumn;
import java.util.Arrays;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class QuestionExcelServiceTest {
    @Mock
    private DataFormatter dataFormatter;
    @Mock
    private ExcelParserService excelParserService;
    @Spy
    @InjectMocks
    private QuestionExcelServiceImpl questionExcelService;
    private final HashMap<ExcelColumn, Integer> columnIndexes = getIndexes(QuestionExcelColumn.values());
    private final DataFormatter cellFormatter = new DataFormatter();
    private final MockMultipartFile multipartFile = new MockMultipartFile("file", new byte[0]);
    private QuestionExcel questionExcel;
    private AnswerExcel answerExcel;

    @BeforeEach
    void setUp() {
        questionExcel = getQuestionExcel();
        answerExcel = getAnswerExcel();
    }

    @Test
    @DisplayName("parseExcel should return ParsingMistakes with mistakes without setting QuestionExcel or AnswerExcel")
    void givenParsingMistakes_whenParseExcel_thenDoNotSetQuestionsInfo() {
        when(excelParserService.excelToList(multipartFile))
                .thenReturn(singletonList(singletonList(getEmptyCell())));
        when(excelParserService.getColumnIndexes(anyList(), eq(QuestionExcelColumn.values())))
                .thenReturn(columnIndexes);
        when(excelParserService
                .validateColumnsPresent(anyList(), eq(QuestionExcelColumn.values()), eq(columnIndexes)))
                .thenReturn(singletonList(new ExcelParsingMistake()));

        ExcelQuestionParsingResponse parsingResponse = questionExcelService.parseExcel(multipartFile);

        verify(questionExcelService, never()).createQuestions(anyList());
        verify(questionExcelService, never()).createAnswers(anyList());
        assertTrue(parsingResponse.getQuestionsInfo().isEmpty());
        assertTrue(parsingResponse.getAnswersInfo().isEmpty());
    }

    @Test
    @DisplayName("parseExcel should return correct QuestionExcel and AnswerExcel after parsing valid row of sheet")
    void givenValidRow_whenParseExcel_thenReturnQuestionExcel() {
        when(excelParserService.excelToList(multipartFile))
                .thenReturn(getValidRowCertificateExcel());
        when(excelParserService.getColumnIndexes(anyList(), eq(QuestionExcelColumn.values())))
                .thenReturn(columnIndexes);
        when(excelParserService
                .validateColumnsPresent(anyList(), eq(QuestionExcelColumn.values()), eq(columnIndexes)))
                .thenReturn(emptyList());
        when(dataFormatter.formatCellValue(ArgumentMatchers.any(Cell.class)))
                .then(invocation -> cellFormatter.formatCellValue((Cell) invocation.getArguments()[0]));

        ExcelQuestionParsingResponse parsingResponse = questionExcelService.parseExcel(multipartFile);

        assertEquals(singletonList(questionExcel), parsingResponse.getQuestionsInfo());
        assertEquals(Arrays.asList(answerExcel, answerExcel), parsingResponse.getAnswersInfo());
        assertTrue(parsingResponse.getQuestionParsingMistakes().isEmpty());
    }

    private List<List<Cell>> getValidRowCertificateExcel() {
        List<List<Cell>> rows =
                getListOfEmptyCells(getEmptySheet(3, QuestionExcelColumn.values().length));
        List<Cell> row = rows.get(1);
        row.get(QuestionExcelColumn.TITLE.ordinal()).setCellValue(questionExcel.getTitle());
        row.get(QuestionExcelColumn.DESCRIPTION.ordinal()).setCellValue(questionExcel.getDescription());
        row.get(QuestionExcelColumn.TYPE.ordinal()).setCellValue(questionExcel.getType());
        row.get(QuestionExcelColumn.CATEGORY.ordinal()).setCellValue(questionExcel.getCategory());
        row.get(QuestionExcelColumn.ANSWERS.ordinal()).setCellValue(answerExcel.getText());
        row.get(QuestionExcelColumn.CORRECT.ordinal()).setCellValue(answerExcel.getIsCorrect());
        row.get(QuestionExcelColumn.VALUE.ordinal()).setCellValue(answerExcel.getValue());

        row = rows.get(2);
        row.subList(0, 4).clear();
        row.get(0).setCellValue(answerExcel.getText());
        row.get(1).setCellValue(answerExcel.getIsCorrect());
        row.get(2).setCellValue(answerExcel.getValue());
        return rows;
    }
}
