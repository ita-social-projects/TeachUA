package com.softserve.teachua;

import java.time.LocalDate;
import java.util.Random;
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

    //DurationEntityService
    public static final String DURATION_ENTITY_ALREADY_EXIST =
        "DurationEntity already exist with startDate: %s endDate: %s";
    public static final long DURATION_ENTITY_ID = 1L;
    public static final LocalDate START_DATE = LocalDate.of(9999, 1, 1);
    public static final LocalDate END_DATE = LocalDate.of(9999, 12, 31);
    public static final int ONE_TIMES_VERIFY = 1;
    public static final int TWO_TIMES_VERIFY = 2;
    public static final int NUM_OF_GENERATED_ELEMENTS = 10;
    //UserChallengeStatusService
    public static final Random RANDOM = new Random();
    public static final TestUserChallengeStatusEnum[] ARRAY_OF_STATUSES = TestUserChallengeStatusEnum.values();
    public static final long USER_CHALLENGE_STATUS_ID = 1L;
    public static final int NUM_OF_METHOD_CALLS = 1;
    public static final TestUserChallengeStatusEnum ADDED = TestUserChallengeStatusEnum.ADDED;
    public static final String USER_CHALLENGE_STATUS_NOT_FOUND_BY_ID = "UserChallengeStatus not found by id: %s";
    public static final String USER_CHALLENGE_STATUS_NOT_FOUND_BY_STATUS_NAME =
        "UserChallengeStatus not found by statusName: %s";
    public static final String USER_CHALLENGE_STATUS_NOT_FOUND_STATUSES = "UserChallengeStatus not found";
    public static final String EXISTING_STATUS_NAME = "ADDED";
    public static final String USER_CHALLENGE_STATUS_DELETING_ERROR =
        "Can't delete userChallengeStatus cause of relationship";
    public static final String USER_CHALLENGE_STATUS_ALREADY_EXIST =
        "UserChallengeStatus already exist with statusName: %s";

    //ChallengeDurationService

    public static final long USER_ID = 1L;
    public static final long USER_CHALLENGE_ID = 1L;
    public static final boolean CHALLENGE_IS_ACTIVE = true;
    public static final boolean USER_EXIST = true;
    public static final long ROLE_ID = 1L;
    public static final long FIRST_TASK_ID = 1L;

    public static final TestUserChallengeStatusEnum USER_CHALLENGE_STATUS_ACTIVE = TestUserChallengeStatusEnum.ACTIVE;
    public static final String CHALLENGE_DURATION_FOR_ADMIN_NOT_FOUND =
        "ChallengeDurationForAdmin not found";
    public static final String CHALLENGE_DURATION_FOR_ADMIN_DURATION_STRING_NOT_FOUND =
        "ChallengeDurationForAdminDurationString not found by challengeId: %s";
    public static final String CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DATES =
        "ChallengeDuration not found by challengeId: %s startDate: %s endDate: %s";
    public static final String CHALLENGE_DURATION_DELETING_ERROR =
        "Can't delete ChallengeDuration cause of relationship";

    public static final String CHALLENGE_DURATION_ALREADY_EXIST =
        "Already exist ChallengeDuration %s";
    public static final String CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_ID_AND_DURATION_ID =
        "ChallengeDuration not found by challengeId: %s durationId: %s";
    public static final String CHALLENGE_DURATION_NOT_FOUND_BY_CHALLENGE_DURATION_ID =
        "ChallengeDuration not found by challengeDurationId: %s";
    public static final long CHALLENGE_DURATION_ID = 1L;
    public static final long CHALLENGE_ID = 1L;
    public static final long DURATION_ID = 1L;

    //UserChallengeService

    public static final String CHALLENGE_NAME = "challengeName";
    public static final boolean CHALLENGE_IS_NOT_ACTIVE = false;
    public static final String USER_CHALLENGE_ALREADY_EXIST = "Ви вже зареєстровані на цьому челенджі - %s";
    public static final String USER_CHALLENGE_NOT_FOUND = "UserChallenge not found";
    public static final String CHALLENGE_NOT_FOUND = "NotFound Challenge with id - %s";
    public static final String USER_CHALLENGE_DELETING_ERROR = "Can't delete UserChallenge cause of relationship";

}
