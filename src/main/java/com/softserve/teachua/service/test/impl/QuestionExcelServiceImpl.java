package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.dto.test.answer.answerExcel.AnswerExcel;
import com.softserve.teachua.dto.test.question.questionExcel.ExcelQuestionParsingMistake;
import com.softserve.teachua.dto.test.question.questionExcel.ExcelQuestionParsingResponse;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionExcel;
import com.softserve.teachua.exception.FileUploadException;
import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.repository.test.AnswerRepository;
import com.softserve.teachua.repository.test.QuestionRepository;
import com.softserve.teachua.service.test.QuestionExcelService;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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

    private static final String EMPTY_STRING = "";
    private static final String TITLE = "назва";
    private static final String DESCRIPTION = "опис";
    private static final String TYPE = "тип";
    private static final String CATEGORY = "категорія";
    private static final String ANSWERS = "варіанти";
    private static final String CORRECT = "правильна";
    private static final String VALUE = "оцінка";

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    private final DataFormatter dataFormatter = new DataFormatter();
    private int headerRowIndex = -1;
    private int[] indexes;
    private int count = 0;
    private ExcelQuestionParsingResponse response;

    public QuestionExcelServiceImpl(AnswerRepository answerRepository,
                                    QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public ExcelQuestionParsingResponse parseExcel(MultipartFile multipartFile) {
        response = new ExcelQuestionParsingResponse();
        indexes = new int[]{-1, -1, -1, -1, -1, -1, -1};
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

    private List<Cell> createCells(Row row) {
        List<Cell> rowCells = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Cell cell = row.createCell(i);
            rowCells.add(cell);
        }
        return rowCells;
    }

    private void setColumnsWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 6000);
        sheet.setColumnWidth(6, 6000);
    }

    @Override
    public byte[] exportToExcel() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int rowNumber = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        setColumnsWidth(sheet);

        Row row = sheet.createRow(rowNumber);
        String[] header =
            {"з/п", "Назва", "Опис", "Тип", "Категорія", "Варіанти відповідей", "Правильна відповідь", "Оцінка"};

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < header.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(style);
            count++;
        }
        List<Question> questions = questionRepository.findAllQuestions();

        rowNumber++;
        for (int i = 0; i < questions.size(); i++) {
            row = sheet.createRow(rowNumber);
            List<Cell> cells = createCells(row);
            List<Answer> answers = answerRepository.findAllByQuestionId(questions.get(i).getId());
            Answer answer = answers.get(0);

            for (Cell cell : cells) {
                if (cell.getColumnIndex() == 0) {
                    cell.setCellStyle(style);
                    cell.setCellValue(i + 1);
                }
                if (cell.getColumnIndex() == 1) {
                    cell.setCellValue(questions.get(i).getTitle());
                }
                if (cell.getColumnIndex() == 2) {
                    cell.setCellValue(questions.get(i).getDescription());
                }
                if (cell.getColumnIndex() == 3) {
                    cell.setCellStyle(style);
                    cell.setCellValue(questions.get(i).getQuestionType().getTitle());
                }
                if (cell.getColumnIndex() == 4) {
                    cell.setCellValue(questions.get(i).getQuestionCategory().getTitle());
                }
                if (cell.getColumnIndex() == 5) {
                    cell.setCellValue(answer.getText());
                }
                if (cell.getColumnIndex() == 6) {
                    cell.setCellValue(answer.isCorrect());
                }
                if (cell.getColumnIndex() == 7) {
                    cell.setCellStyle(style);
                    cell.setCellValue(answer.getValue());
                }
            }
            answers.remove(answer);
            rowNumber++;
            for (Answer a : answers) {
                row = sheet.createRow(rowNumber);
                cells = createCells(row);

                for (Cell cell : cells) {
                    if (cell.getColumnIndex() == 5) {
                        cell.setCellValue(a.getText());
                    }
                    if (cell.getColumnIndex() == 6) {
                        cell.setCellValue(a.isCorrect());
                    }
                    if (cell.getColumnIndex() == 7) {
                        cell.setCellStyle(style);
                        cell.setCellValue(a.getValue());
                    }
                }
                rowNumber++;
            }
            rowNumber++;
        }

        try {
            workbook.write(byteArrayOutputStream);
            workbook.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private AnswerExcel createAnswer(String title, List<Cell> row) {
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
            questionTitle = title;
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
            response.getQuestionParsingMistakes().add(
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

        if (indexes[4] == -1) {
            response.getQuestionParsingMistakes()
                    .add(new ExcelQuestionParsingMistake(MISSING_COLUMN_ANSWER_ERROR, row.toString(),
                            (long) headerRowIndex));
        }
        if (indexes[5] == -1) {
            response.getQuestionParsingMistakes()
                    .add(new ExcelQuestionParsingMistake(MISSING_COLUMN_CORRECT_ERROR, row.toString(),
                            (long) headerRowIndex));
        }
        if (indexes[6] == -1) {
            response.getQuestionParsingMistakes()
                    .add(new ExcelQuestionParsingMistake(MISSING_COLUMN_VALUES_ERROR, row.toString(),
                            (long) headerRowIndex));
        }
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
