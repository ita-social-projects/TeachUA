package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.dto.test.answer.answerExcel.AnswerExcel;
import com.softserve.teachua.dto.test.question.questionExcel.ExcelQuestionParsingMistake;
import com.softserve.teachua.dto.test.question.questionExcel.ExcelQuestionParsingResponse;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionExcel;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.service.test.QuestionExcelService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class QuestionExcelServiceImpl implements QuestionExcelService {

    protected static final String FILE_NOT_FOUND_EXCEPTION =
        "File %s could not be found";
    protected static final String FILE_NOT_READ_EXCEPTION =
        "File %s could not be read";
    protected static final String FILE_NOT_CLOSE_EXCEPTION =
        "File %s could not be closed";
    private static final String FILE_LOAD_EXCEPTION =
        "Could not load excel file";
    private static final String MISSING_HEADER_ROW =
        "Відсутній рядок з назвами колонок";
    private static final String MISSING_COLUMN_TITLE_ERROR =
        "Відсутня колонка з назвою запитання";
    private static final String MISSING_COLUMN_DESCRIPTION_ERROR =
        "Відсутня колонка з описом запитання";
    private static final String MISSING_COLUMN_TYPE_ERROR =
        "Відсутня колонка з типом запитання";
    private static final String MISSING_COLUMN_CATEGORY_ERROR =
        "Відсутня колонка з категорією запитання";
    private static final String MISSING_COLUMN_ANSWER_ERROR =
        "Відсутня колонка з варіантами відповідь до запитання";
    private static final String MISSING_COLUMN_CORRECT_ERROR =
        "Відсутня колонка з правильними відповідями до запитання";
    private static final String MISSING_COLUMN_VALUES_ERROR =
        "Відсутня колонка з оцінками для запитання";

    private final static String EMPTY_STRING = "";
    private final static String TITLE = "назва";
    private final static String DESCRIPTION = "опис";
    private final static String TYPE = "тип";
    private final static String CATEGORY = "категорія";
    private final static String ANSWERS = "варіанти";
    private final static String CORRECT = "правильна";
    private final static String VALUE = "оцінка";

    private final DataFormatter dataFormatter = new DataFormatter();
    private int headerRowIndex = -1;
    private int[] indexes;
    private ExcelQuestionParsingResponse response;

    @Override
    public ExcelQuestionParsingResponse parseExcel(MultipartFile multipartFile) {
        response = new ExcelQuestionParsingResponse();
        indexes = new int[] {-1, -1, -1, -1, -1, -1, -1};
        try (InputStream inputStream = multipartFile.getInputStream()) {
            List<List<Cell>> list = excelToList(inputStream);
            response.setQuestionsInfo(createQuestions(list));
            response.setAnswersInfo(createAnswers(list));
        } catch (IOException e) {
            log.error("Upload excel error, " + FILE_LOAD_EXCEPTION);
            throw new FileUploadException(FILE_LOAD_EXCEPTION);
        }

        return response;
    }

    private List<List<Cell>> excelToList(InputStream inputStream) {
        List<List<Cell>> allCells = new ArrayList<List<Cell>>();
        XSSFWorkbook workbook;
        Sheet sheet;
        try {
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(FILE_NOT_FOUND_EXCEPTION);
        } catch (IOException e) {
            throw new RuntimeException(FILE_NOT_READ_EXCEPTION);
        }
        for (Row row : sheet) {
            if (isRowEmpty(row)) {
                continue;
            }
            Iterator<Cell> cellIterator = row.iterator();
            List<Cell> allRowCells = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                if (!isColumnEmpty(sheet, currentCell.getColumnIndex())) {
                    String cell = dataFormatter.formatCellValue(currentCell);
                    if (cell.toLowerCase().contains(TITLE) || cell.toLowerCase().contains(DESCRIPTION)
                        || cell.toLowerCase().contains(TYPE) || cell.toLowerCase().contains(ANSWERS)

                    ) {
                        headerRowIndex = allCells.size();
                    }
                    allRowCells.add(currentCell);
                }
            }

            allCells.add(allRowCells);

        }

        if (headerRowIndex == -1) {
            response.getQuestionParsingMistakes()
                .add(new ExcelQuestionParsingMistake(MISSING_HEADER_ROW, EMPTY_STRING, null));
            return allCells;
        } else {
            setIndexes(allCells.get(headerRowIndex));
        }
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException(FILE_NOT_CLOSE_EXCEPTION);
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(FILE_NOT_CLOSE_EXCEPTION);
            }
        }
        return allCells;
    }

    private List<QuestionExcel> createQuestions(List<List<Cell>> rows) {
        List<QuestionExcel> result = new ArrayList<>();
        if (headerRowIndex != -1) {
            rows.remove(headerRowIndex);
        }

        for (List<Cell> row : rows) {

            if (row.size() > 3) {
                result.add(createQuestion(row));
            }
        }

        return result;
    }


    private QuestionExcel createQuestion(List<Cell> row) {
        List<Cell> data = new ArrayList<>(row);
        String title = null;
        String description = " ";
        String type = null;
        String category = null;


        if (indexes[0] != -1) {
            title = dataFormatter.formatCellValue(data.get(indexes[0])).trim();
        }

        if (indexes[1] != -1) {
            description = dataFormatter.formatCellValue(data.get(indexes[1])).trim();
        }
        if (indexes[2] != -1) {
            type = dataFormatter.formatCellValue(data.get(indexes[2])).trim();
        }
        if (indexes[3] != -1) {
            category = dataFormatter.formatCellValue(data.get(indexes[3])).trim();
        }


        return QuestionExcel.builder().title(title).description(description).type(type).category(category).build();
    }


    private List<AnswerExcel> createAnswers(List<List<Cell>> rows) {
        List<AnswerExcel> result = new ArrayList<>();
        String questionTitle = null;

        for (List<Cell> row : rows) {
            if (row.size() > 3) {
                questionTitle = String.valueOf(row.get(indexes[0]));
                result.add(createAnswer(questionTitle, row));
            } else {
                result.add(createAnswer(questionTitle, row));

            }
        }

        return result;
    }


    private AnswerExcel createAnswer(String Title, List<Cell> row) {
        String questionTitle = null;

        List<Cell> data = new ArrayList<>(row);

        String text = null;
        String isCorrect = null;
        int value = 0;

        if (row.size() > 3) {


            if (indexes[0] != -1) {

                questionTitle = dataFormatter.formatCellValue(data.get(indexes[0])).trim();
            }
            if (indexes[4] != -1) {
                text = dataFormatter.formatCellValue(data.get(indexes[4])).trim();
            }
            if (indexes[5] != -1) {
                isCorrect = dataFormatter.formatCellValue(data.get(indexes[5])).trim();
            }
            if (indexes[6] != -1) {
                value = Integer.parseInt(dataFormatter.formatCellValue(data.get(indexes[6])).trim());
            }
        } else {

            questionTitle = Title;
            if (indexes[4] != -1) {
                text = dataFormatter.formatCellValue(data.get(0)).trim();
            }
            if (indexes[5] != -1) {
                isCorrect = dataFormatter.formatCellValue(data.get(1)).trim();
            }
            if (indexes[6] != -1) {
                value = Integer.parseInt(dataFormatter.formatCellValue(data.get(2)).trim());
            }

        }

        return AnswerExcel.builder()
            .questionTitle(questionTitle)
            .text(text)
            .isCorrect(isCorrect)
            .value(value)
            .build();

    }


    private void setIndexes(List<Cell> row) {
        for (int i = 0; i < row.size(); i++) {
            String cell = dataFormatter.formatCellValue(row.get(i)).toLowerCase();
            if (cell.contains(TITLE)) {
                indexes[0] = i;
            }
            if (cell.contains(DESCRIPTION)) {
                indexes[1] = i;
            }
            if (cell.contains(TYPE)) {
                indexes[2] = i;
            }
            if (cell.contains(CATEGORY)) {
                indexes[3] = i;
            }
            if (cell.contains(ANSWERS)) {
                indexes[4] = i;
            }
            if (cell.contains(CORRECT)) {
                indexes[5] = i;
            }
            if (cell.contains(VALUE)) {
                indexes[6] = i;
            }

        }

        if (indexes[0] == -1) {
            response.getQuestionParsingMistakes()
                .add(
                    new ExcelQuestionParsingMistake(MISSING_COLUMN_TITLE_ERROR, row.toString(), (long) headerRowIndex));
        }

        if (indexes[2] == -1) {
            response.getQuestionParsingMistakes()
                .add(new ExcelQuestionParsingMistake(MISSING_COLUMN_TYPE_ERROR, row.toString(),
                    (long) headerRowIndex));
        }
        if (indexes[3] == -1) {
            response.getQuestionParsingMistakes()
                .add(new ExcelQuestionParsingMistake(MISSING_COLUMN_CATEGORY_ERROR, row.toString(),
                    (long) headerRowIndex));
        }

//        if (indexes[4] == -1) {
//            response.getQuestionParsingMistakes()
//                .add(new ExcelQuestionParsingMistake(MISSING_COLUMN_ANSWER_ERROR, row.toString(),
//                    (long) headerRowIndex));
//        }
//        if (indexes[5] == -1) {
//            response.getQuestionParsingMistakes()
//                .add(new ExcelQuestionParsingMistake(MISSING_COLUMN_CORRECT_ERROR, row.toString(),
//                    (long) headerRowIndex));
//        }
//        if (indexes[6] == -1) {
//            response.getQuestionParsingMistakes()
//                .add(new ExcelQuestionParsingMistake(MISSING_COLUMN_VALUES_ERROR, row.toString(),
//                    (long) headerRowIndex));
//        }

    }

    private boolean isRowEmpty(Row row) {
        boolean isEmpty = true;
        DataFormatter dataFormatter = new DataFormatter();
        if (row != null) {
            for (Cell cell : row) {
                if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
                    isEmpty = false;
                    break;
                }
            }
        }
        return isEmpty;
    }

    private boolean isColumnEmpty(Sheet sheet, int columnIndex) {
        for (Row row : sheet) {
            Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null) {
                return false;
            }
        }
        return true;
    }


}
