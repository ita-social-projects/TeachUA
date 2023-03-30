package com.softserve.teachua;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.softserve.teachua.TestConstants.CERTIFICATE_DATES_DURATION;
import static com.softserve.teachua.TestConstants.CERTIFICATE_DATES_END_DATE;
import static com.softserve.teachua.TestConstants.CERTIFICATE_DATES_START_DATE;
import static com.softserve.teachua.TestConstants.CERTIFICATE_ISSUE_DATE;
import static com.softserve.teachua.TestConstants.CERTIFICATE_TEMPLATE_NAME;
import static com.softserve.teachua.TestConstants.CERTIFICATE_TEMPLATE_PROPERTIES;
import static com.softserve.teachua.TestConstants.CERTIFICATE_TYPE_CODE_NUMBER;
import static com.softserve.teachua.TestConstants.CERTIFICATE_TYPE_ID;
import static com.softserve.teachua.TestConstants.CERTIFICATE_TYPE_NAME;
import static com.softserve.teachua.TestConstants.CORRECT;
import static com.softserve.teachua.TestConstants.COURSE_DESCRIPTION;
import static com.softserve.teachua.TestConstants.COURSE_NUMBER;
import static com.softserve.teachua.TestConstants.FILE_PATH;
import static com.softserve.teachua.TestConstants.FIRST_NAME;
import static com.softserve.teachua.TestConstants.HOURS;
import static com.softserve.teachua.TestConstants.INTEGER_ID;
import static com.softserve.teachua.TestConstants.LAST_NAME;
import static com.softserve.teachua.TestConstants.LONG_ID;
import static com.softserve.teachua.TestConstants.NOT_EMPTY_STRING;
import static com.softserve.teachua.TestConstants.PASSWORD;
import static com.softserve.teachua.TestConstants.PHONE;
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
import static com.softserve.teachua.TestConstants.USER_ID;
import static com.softserve.teachua.TestConstants.USER_NAME;
import static com.softserve.teachua.TestConstants.USER_ROLE;
import static com.softserve.teachua.TestConstants.VARIANT;
import com.softserve.teachua.constants.excel.ExcelColumn;
import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificate.CertificateUserResponse;
import com.softserve.teachua.dto.certificate.CertificateVerificationResponse;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificate_excel.CertificateExcel;
import com.softserve.teachua.dto.googleapis.QuizResult;
import com.softserve.teachua.dto.test.answer.answerExcel.AnswerExcel;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionExcel;
import com.softserve.teachua.dto.user.UserLogin;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.model.CertificateType;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.User;
import com.softserve.teachua.security.UserPrincipal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.SheetBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class TestUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static User getUser() {
        return User.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .role(getUserRole())
                .password(PASSWORD)
                .phone(PHONE)
                .status(true)
                .build();
    }

    private static Role getUserRole() {
        return Role.builder()
                .id(INTEGER_ID)
                .name(USER_ROLE)
                .build();
    }

    public static UserLogin getUserLogin() {
        return new UserLogin(getUser().getEmail(), getUser().getPassword());
    }

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
                .duration(CERTIFICATE_DATES_DURATION)
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
                .dateIssued(CERTIFICATE_ISSUE_DATE)
                .build();
    }

    public static CertificateDataRequest getCertificateDataRequest() {
        List<CertificateExcel> excelList = new ArrayList<>();
        excelList.add(getCertificateExcel());

        return CertificateDataRequest.builder()
                .type(CERTIFICATE_TYPE_CODE_NUMBER)
                .hours(HOURS)
                .startDate(CERTIFICATE_DATES_START_DATE)
                .endDate(CERTIFICATE_DATES_END_DATE)
                .courseNumber(COURSE_NUMBER)
                .excelList(excelList)
                .build();
    }

    public static UserPrincipal getUserPrincipal(User user) {
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(
                                Optional.ofNullable(user.getRole())
                                        .map(Role::getName)
                                        .orElse("ROLE_USER")
                        )
                )
        );
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

    public static CertificateByTemplateTransfer getCertificateByTemplateTransfer()
            throws JsonProcessingException {
        String json = "{\"fieldsList\":[\"fullName\",\"learningForm\",\"Номер курсу\",\"countOfHours\",\"issueDate\","
                + "\"Електронна пошта\",\"duration\"],\"fieldPropertiesList\":[\"String\",\"String\",\"int\","
                + "\"int\",\"date\",\"String\",\"String\"],\"templateName\":\"1673724092154.pdf\",\"values\":"
                + "\"{\\\"fullName\\\":\\\"\\\",\\\"learningForm\\\":\\\"дистанційна\\\",\\\"Номер курсу\\\":"
                + "\\\"1\\\",\\\"countOfHours\\\":\\\"99\\\",\\\"issueDate\\\":\\\"05.02.2023\\\","
                + "\\\"Електронна пошта\\\":\\\"\\\", \\\"duration\\\":\\\"duration\\\"}\","
                + "\"columnHeadersList\":[\"№ п/п\",\"Прізвище, ім'я, по батькові отримувача\","
                + "\"Номер сертифіката\",\"Дата видачі\",\"Електронна адреса\",\"Примітки\"],\"excelContent\""
                + ":[[\"1\",\"Денисюк-Стасюк Олександр-Іван\",\"1010000001\",\"30.10.2023\",\"email@gmail.com\""
                + ",\"Виданий без нанесення номера\"]],\"excelColumnsOrder\":[\"Прізвище, ім'я, по батькові "
                + "отримувача\",\"№ п/п\",\"Номер сертифіката\",\"Дата видачі\",\"Примітки\","
                + "\"Електронна адреса\", \"\"],\"googleFormResults\":[]}";
        return objectMapper.readValue(json, CertificateByTemplateTransfer.class);
    }

    public static CertificateByTemplateTransfer getInvalidCertificateByTemplateTransfer()
            throws JsonProcessingException {
        String json = "{\"fieldsList\":[\"fullName\",\"learningForm\",\"Номер курсу\",\"countOfHours\",\"issueDate\","
                + "\"Електронна пошта\",\"duration\",\"course_number\"],\"fieldPropertiesList\":[\"String\",\"String\","
                + "\"int\",\"int\",\"date\",\"String\",\"String\",\"int\"],\"templateName\":\"1673724092154.pdf\","
                + "\"values\":\"{\\\"fullName\\\":\\\"\\\",\\\"learningForm\\\":\\\"дистанційна\\\",\\\"Номер"
                + " курсу\\\":\\\"номер\\\",\\\"countOfHours\\\":\\\"-10\\\",\\\"issueDate\\\":\\\"2023/02/05\\\","
                + "\\\"Електронна пошта\\\":\\\"\\\",\\\"duration\\\":\\\"\\\",\\\"course_number\\\":\\\"99\\\"}\","
                + "\"columnHeadersList\":[\"№ п/п\",\"Прізвище, ім'я, по батькові отримувача\",\"Номер "
                + "сертифіката\",\"Дата видачі\",\"Електронна адреса\",\"Примітки\",\"Статус\"],"
                + "\"excelContent\":[[\"1\",\"Денисюк-Стасюк  Олександр-Іван \",\"1010000001\",\"05.02.2023\","
                + "\"email@@gmail.com\",\"Виданий без нанесення номера\",\"\"]],\"excelColumnsOrder\":[\"Прізвище, "
                + "ім'я, по батькові отримувача\",\"№ п/п\",\"Номер сертифіката\",\"Дата видачі\","
                + "\"Примітки\",\"Електронна адреса\",\"Статус\"]}";
        return objectMapper.readValue(json, CertificateByTemplateTransfer.class);
    }

    public static List<QuizResult> getQuizResults() {
        QuizResult quizResult1 = QuizResult.builder()
                .userEmail("grygor@test.com")
                .fullName("Григоренко Григорій Григорович")
                .totalScore(5)
                .build();
        QuizResult quizResult2 = QuizResult.builder()
                .userEmail("andriy@test.com")
                .fullName("Андрійко Андрій Андрійович")
                .totalScore(1)
                .build();
        QuizResult quizResult3 = QuizResult.builder()
                .userEmail("ivan@test.com")
                .fullName("Іванко Іван Іванович")
                .totalScore(3)
                .build();

        List<QuizResult> results = new ArrayList<>();
        results.add(quizResult1);
        results.add(quizResult2);
        results.add(quizResult3);

        return results;
    }
    public static List<QuizResult> getInvalidQuizResults() {
        QuizResult quizResult1 = QuizResult.builder()
                .userEmail("grygortest.com")
                .fullName("Григоренко  Григорій Григорович ")
                .totalScore(5)
                .build();
        QuizResult quizResult2 = QuizResult.builder()
                .userEmail("@test.com")
                .fullName("Андрійко Андрій  Андрійович")
                .totalScore(1)
                .build();
        QuizResult quizResult3 = QuizResult.builder()
                .userEmail("ivan@")
                .fullName("Іванко Іван Іванович")
                .totalScore(3)
                .build();
        List<QuizResult> results = new ArrayList<>();
        results.add(quizResult1);
        results.add(quizResult2);
        results.add(quizResult3);
        return results;
    }
}
