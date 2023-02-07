package com.softserve.teachua;

import static com.softserve.teachua.TestConstants.CERTIFICATE_TEMPLATE_NAME;
import static com.softserve.teachua.TestConstants.CERTIFICATE_TEMPLATE_PROPERTIES;
import static com.softserve.teachua.TestConstants.CERTIFICATE_TYPE_CODE_NUMBER;
import static com.softserve.teachua.TestConstants.CERTIFICATE_TYPE_ID;
import static com.softserve.teachua.TestConstants.CERTIFICATE_TYPE_NAME;
import static com.softserve.teachua.TestConstants.COURSE_DESCRIPTION;
import static com.softserve.teachua.TestConstants.COURSE_NUMBER;
import static com.softserve.teachua.TestConstants.DURATION;
import static com.softserve.teachua.TestConstants.FILE_PATH;
import static com.softserve.teachua.TestConstants.HOURS;
import static com.softserve.teachua.TestConstants.INTEGER_ID;
import static com.softserve.teachua.TestConstants.LONG_ID;
import static com.softserve.teachua.TestConstants.NOT_EMPTY_STRING;
import static com.softserve.teachua.TestConstants.PICTURE_PATH;
import static com.softserve.teachua.TestConstants.PROJECT_DESCRIPTION;
import static com.softserve.teachua.TestConstants.QUESTION_CATEGORY;
import static com.softserve.teachua.TestConstants.QUESTION_DESCRIPTION;
import static com.softserve.teachua.TestConstants.QUESTION_TITLE;
import static com.softserve.teachua.TestConstants.QUESTION_TYPE;
import static com.softserve.teachua.TestConstants.SERIAL_NUMBER;
import static com.softserve.teachua.TestConstants.STRING_DATE;
import static com.softserve.teachua.TestConstants.STUDY_FORM;
import static com.softserve.teachua.TestConstants.UPDATE_DATE;
import static com.softserve.teachua.TestConstants.USER_EMAIL;
import static com.softserve.teachua.TestConstants.USER_NAME;
import static com.softserve.teachua.TestConstants.VARIANT;
import static com.softserve.teachua.TestConstants.CORRECT;
import com.softserve.teachua.dto.certificate.CertificateUserResponse;
import com.softserve.teachua.dto.certificate.CertificateVerificationResponse;
import com.softserve.teachua.dto.certificate_excel.CertificateExcel;
import com.softserve.teachua.dto.test.answer.answerExcel.AnswerExcel;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionExcel;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.model.CertificateType;
import com.softserve.teachua.constants.excel.ExcelColumn;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.SheetBuilder;

public class TestUtils {

    public static Certificate getCertificate() {
        return Certificate.builder()
                .id(LONG_ID)
                .serialNumber(SERIAL_NUMBER)
                .userName(USER_NAME)
                .sendToEmail(USER_EMAIL)
                .updateStatus(UPDATE_DATE)
                .sendStatus(true)
                .template(getCertificateTemplate())
                .dates(getCertificateDates())
                .build();
    }

    public static CertificateUserResponse getCertificateUserResponse() {
        Certificate certificate = getCertificate();
        return CertificateUserResponse.builder()
                .id(certificate.getId())
                .serialNumber(certificate.getSerialNumber())
                .certificateTypeName(certificate.getTemplate().getCertificateType().getName())
                .date(certificate.getDates().getDate())
                .courseDescription(certificate.getTemplate().getCourseDescription())
                .build();
    }

    public static CertificateDates getCertificateDates() {
        return CertificateDates.builder()
                .id(INTEGER_ID)
                .date(STRING_DATE)
                .studyForm(STUDY_FORM)
                .courseNumber(COURSE_NUMBER)
                .duration(DURATION)
                .hours(HOURS)
                .build();
    }

    public static CertificateTemplate getCertificateTemplate() {
        return CertificateTemplate.builder()
                .id(INTEGER_ID)
                .name(CERTIFICATE_TEMPLATE_NAME)
                .certificateType(getCertificateType())
                .filePath(FILE_PATH)
                .courseDescription(COURSE_DESCRIPTION)
                .projectDescription(PROJECT_DESCRIPTION)
                .picturePath(PICTURE_PATH)
                .properties(CERTIFICATE_TEMPLATE_PROPERTIES)
                .build();
    }

    public static CertificateType getCertificateType() {
        return CertificateType.builder()
                .id(CERTIFICATE_TYPE_ID)
                .codeNumber(CERTIFICATE_TYPE_CODE_NUMBER)
                .name(CERTIFICATE_TYPE_NAME)
                .build();
    }

    public static CertificateVerificationResponse getCertificateVerificationResponse() {
        Certificate certificate = getCertificate();
        return CertificateVerificationResponse.builder()
                .certificateTypeName(certificate.getTemplate().getCertificateType().getName())
                .courseDescription(certificate.getTemplate().getCourseDescription())
                .picturePath(certificate.getTemplate().getPicturePath())
                .projectDescription(certificate.getTemplate().getProjectDescription())
                .serialNumber(certificate.getSerialNumber()).userName(certificate.getUserName()).build();
    }

    public static CertificateExcel getCertificateExcel() {
        return CertificateExcel.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .dateIssued(LocalDate.now())
                .build();
    }

    public static QuestionExcel getQuestionExcel() {
        return QuestionExcel.builder()
                .title(QUESTION_TITLE)
                .description(QUESTION_DESCRIPTION)
                .category(QUESTION_CATEGORY)
                .type(QUESTION_TYPE)
                .build();
    }

    public static AnswerExcel getAnswerExcel() {
        return AnswerExcel.builder()
                .questionTitle(QUESTION_TITLE)
                .text(VARIANT)
                .isCorrect(CORRECT)
                .value(2)
                .build();
    }

    public static Sheet getEmptySheet(int rowsNumber, int columnsNumber) {
        try (Workbook workbook = WorkbookFactory.create(false)) {
            return new SheetBuilder(workbook, new Object[rowsNumber][columnsNumber])
                    .setCreateEmptyCells(true)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create empty sheet");
        }
    }

    public static Sheet fillSheet(Sheet sheet) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setCellValue(NOT_EMPTY_STRING);
            }
        }
        return sheet;
    }

    public static List<List<Cell>> getListOfEmptyCells(Sheet sheet) {
        List<List<Cell>> rows = new ArrayList<>();
        for (Row row : sheet) {
            List<Cell> rowList = new ArrayList<>();
            for (Cell cell : row) {
                rowList.add(cell);
            }
            rows.add(rowList);
        }
        return rows;
    }

    public static HashMap<ExcelColumn, Integer> getIndexes(ExcelColumn[] excelColumns) {
        HashMap<ExcelColumn, Integer> indexes = new HashMap<>();
        for (int i = 0; i < excelColumns.length; i++) {
            indexes.put(excelColumns[i], i);
        }
        return indexes;
    }

    public static Row getEmptyRow(int columnsNumber) {
        return getEmptySheet(1, columnsNumber).getRow(0);
    }

    public static Cell getEmptyCell() {
        return getEmptyRow(1).getCell(0);
    }
}
