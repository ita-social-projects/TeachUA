package com.softserve.teachua;

import java.time.LocalDate;
import org.springframework.mock.web.MockMultipartFile;

public final class TestConstants {
    public static final String EMPTY_STRING = "";
    public static final String NOT_EMPTY_STRING = "Value";
    public static final Long LONG_ID = 1L;
    public static final Integer INTEGER_ID = 2;
    public static final String USER_EMAIL = "admin@gmail.com";
    public static final Long SERIAL_NUMBER = 3010000001L;
    public static final String USER_NAME = "Власник Сертифікату";
    public static final LocalDate UPDATE_DATE = LocalDate.now();
    public static final String STRING_DATE = "01.11.2022";
    public static final String STUDY_FORM = "дистанційна";
    public static final String COURSE_NUMBER = "10";
    public static final String DURATION = "з 4 по 21 жовтня 2022 року";
    public static final int HOURS = 40;
    public static final String CERTIFICATE_TEMPLATE_NAME = "Єдині учасник";
    public static final String FILE_PATH = "/certificates/templates/jedyni_participant_template.jrxml";
    public static final String FILE_PATH_PDF = "1673724092154.pdf";
    public static final String COURSE_DESCRIPTION = "Всеукраїнський курс підтримки в переході на українську мову";
    public static final String PROJECT_DESCRIPTION = "Курс створений та реалізований у рамках проєкту Єдині";
    public static final String PICTURE_PATH = "/static/images/certificate/validation/jedyni_banner.png";
    public static final String CERTIFICATE_TEMPLATE_PROPERTIES =
            "{\"id\":\"serial_number\",\"fullName\":\"user_name\",\"issueDate\":\"date\",\"countOfHours\":\"hours\","
                    + "\"learningForm\":\"study_form\",\"image\":\"qrCode_white\",\"duration\":\"duration\"}";
    public static final Integer CERTIFICATE_TYPE_ID = 3;
    public static final Integer CERTIFICATE_TYPE_CODE_NUMBER = 3;
    public static final String CERTIFICATE_TYPE_NAME = "Учасник";
    public static final String QUESTION_TITLE = "Укажіть дієприкметник";
    public static final String QUESTION_DESCRIPTION = "Вкажіть правильний варіант";
    public static final String QUESTION_CATEGORY = "Нова Категорія";
    public static final String QUESTION_TYPE = "Radio";
    public static final String VARIANT = "Вигравши";
    public static final String CORRECT = "TRUE";
    public static final MockMultipartFile MOCK_MULTIPART_FILE = new MockMultipartFile("file", new byte[0]);
}
