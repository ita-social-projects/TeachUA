package com.softserve.teachua;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.softserve.teachua.constants.excel.ExcelColumn;
import com.softserve.teachua.dto.certificate.CertificateUserResponse;
import com.softserve.teachua.dto.certificate.CertificateVerificationResponse;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificate_excel.CertificateExcel;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationAdd;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDelete;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationDeleteResponse;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationExistUserResponse;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdmin;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationLocalDate;
import com.softserve.teachua.dto.challenge_duration.ChallengeDurationForAdminDurationString;
import com.softserve.teachua.dto.duration_entity.DurationEntityResponse;
import com.softserve.teachua.dto.test.answer.answerExcel.AnswerExcel;
import com.softserve.teachua.dto.test.question.questionExcel.QuestionExcel;
import com.softserve.teachua.dto.user_challenge.UserChallengeCreateResponse;
import com.softserve.teachua.dto.user_challenge.UserChallengeDeleteResponse;
import com.softserve.teachua.dto.user_challenge.UserChallengeUpdateResponse;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminDelete;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGet;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetByChallengeIdDurationId;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminGetChallengeDuration;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminNotRegisteredUser;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminRegisteredUser;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForAdminUpdate;
import com.softserve.teachua.dto.user_challenge.admin.UserChallengeForUserCreate;
import com.softserve.teachua.dto.user_challenge.exist.UserChallengeForExist;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileDelete;
import com.softserve.teachua.dto.user_challenge.profile.UserChallengeForProfileGet;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserCreateWithDate;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetLocalDate;
import com.softserve.teachua.dto.user_challenge.registration.UserChallengeForUserGetString;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusAdd;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusDelete;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusForOption;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusGet;
import com.softserve.teachua.dto.user_challenge_status.UserChallengeStatusUpdate;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.model.CertificateType;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.ChallengeDuration;
import com.softserve.teachua.model.DurationEntity;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.UserChallenge;
import com.softserve.teachua.model.UserChallengeStatus;
import com.softserve.teachua.model.archivable.ChallengeDurationArch;
import com.softserve.teachua.model.archivable.DurationEntityArch;
import com.softserve.teachua.model.archivable.UserChallengeArch;
import com.softserve.teachua.model.archivable.UserChallengeStatusArch;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.SheetBuilder;

import static com.softserve.teachua.TestConstants.*;

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

    public static CertificateByTemplateTransfer getCertificateByTemplateTransfer()
        throws JsonProcessingException {
        String json = "{\"fieldsList\":[\"fullName\",\"learningForm\",\"Номер курсу\",\"countOfHours\",\"issueDate\","
            + "\"Електронна пошта\",\"duration\"],\"fieldPropertiesList\":[\"String\",\"String\",\"int\","
            + "\"int\",\"date\",\"String\",\"String\"],\"templateName\":\"1673724092154.pdf\","
            + "\"values\":\"{\\\"fullName\\\":\\\"\\\",\\\"learningForm\\\":\\\"дистанційна\\\",\\\"Номер"
            + " курсу\\\":\\\"1\\\",\\\"countOfHours\\\":\\\"99\\\",\\\"issueDate\\\":\\\"05.02.2023\\\","
            + "\\\"Електронна пошта\\\":\\\"\\\", \\\"duration\\\":\\\"duration\\\"}\","
            + "\"columnHeadersList\":[\"№ п/п\",\"Прізвище, ім'я, по батькові отримувача\",\"Номер "
            + "сертифіката\",\"Дата видачі\",\"Електронна адреса\",\"Примітки\"],"
            + "\"excelContent\":[[\"1\",\"Денисюк-Стасюк Олександр-Іван\",\"1010000001\",\"30.10.2023\","
            + "\"email@gmail.com\",\"Виданий без нанесення номера\"]],\"excelColumnsOrder\":[\"Прізвище, "
            + "ім'я, по батькові отримувача\",\"№ п/п\",\"Номер сертифіката\",\"Дата видачі\","
            + "\"Примітки\",\"Електронна адреса\", \"\"]}";
        return new ObjectMapper().readValue(json, CertificateByTemplateTransfer.class);
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
        return new ObjectMapper().readValue(json, CertificateByTemplateTransfer.class);
    }

    //DurationEntityService
    public static List<DurationEntity> getDurationEntityList(
        int numOfGeneratedElements, long firstId, LocalDate firstStartDate, LocalDate firstEndDate) {
        List<DurationEntity> generatedDurationEntityList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            DurationEntity generatedDurationEntity =
                DurationEntity.builder()
                    .id(firstId + i)
                    .startDate(firstStartDate.plusDays(i))
                    .endDate(firstEndDate.plusDays(i))
                    .build();
            generatedDurationEntityList.add(generatedDurationEntity);
        }
        return generatedDurationEntityList;
    }

    public static List<DurationEntityResponse> getDurationEntityResponseList(
        int numOfGeneratedElements, LocalDate firstStartDate, LocalDate firstEndDate) {
        List<DurationEntityResponse> generatedDurationEntityResponseList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            DurationEntityResponse generatedDurationEntityResponse =
                DurationEntityResponse
                    .builder()
                    .startDate(firstStartDate.plusDays(i))
                    .endDate(firstEndDate.plusDays(i))
                    .build();
            generatedDurationEntityResponseList.add(generatedDurationEntityResponse);
        }
        return generatedDurationEntityResponseList;
    }

    public static List<DurationEntityResponse> getDurationEntityResponseListWithRepeats(
        int numOfGeneratedElements, LocalDate firstStartDate, LocalDate firstEndDate) {
        List<DurationEntityResponse> generatedDurationEntityResponseList =
            getDurationEntityResponseList(numOfGeneratedElements, firstStartDate, firstEndDate);
        List<DurationEntityResponse> resultListWIthRepeats = new ArrayList<>();
        resultListWIthRepeats.addAll(generatedDurationEntityResponseList);
        resultListWIthRepeats.addAll(generatedDurationEntityResponseList);
        return resultListWIthRepeats;
    }

    public static Set<DurationEntity> getDurationEntitySet(
        int numOfGeneratedElements, long firstId, LocalDate firstStartDate, LocalDate firstEndDate) {
        Set<DurationEntity> generatedDurationEntityList = new LinkedHashSet<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            DurationEntity generatedDurationEntity =
                DurationEntity
                    .builder()
                    .id(firstId + i)
                    .startDate(firstStartDate.plusDays(i))
                    .endDate(firstEndDate.plusDays(i))
                    .build();
            generatedDurationEntityList.add(generatedDurationEntity);
        }
        return generatedDurationEntityList;
    }

    public static Set<DurationEntity> getDurationEntitySetWithoutZeroId(
        int numOfGeneratedElements, LocalDate firstStartDate, LocalDate firstEndDate) {
        Set<DurationEntity> generatedDurationEntityList = new LinkedHashSet<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            DurationEntity generatedDurationEntity =
                DurationEntity
                    .builder()
                    .id(0L)
                    .startDate(firstStartDate.plusDays(i))
                    .endDate(firstEndDate.plusDays(i))
                    .build();
            generatedDurationEntityList.add(generatedDurationEntity);
        }
        return generatedDurationEntityList;
    }

    public static DurationEntity getDurationEntity(
        long id, LocalDate startDate, LocalDate endDate) {
        return DurationEntity
            .builder()
            .id(id)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    public static DurationEntityArch getDurationEntityArch(
        long id, LocalDate startDate, LocalDate endDate) {
        return DurationEntityArch
            .builder()
            .id(id)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    //UserChallengeStatusService
    public static String getRandomUserChallengeStatus() {
        int enumLength = ARRAY_OF_STATUSES.length;
        return ARRAY_OF_STATUSES[RANDOM.nextInt(enumLength)].name();
    }

    public static UserChallengeStatus getUserChallengeStatus(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        return UserChallengeStatus
            .builder()
            .id(id)
            .statusName(testUserChallengeStatusEnum.name())
            .build();
    }

    public static UserChallengeStatusGet getUserChallengeStatusGet(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        return UserChallengeStatusGet
            .builder()
            .id(id)
            .statusName(testUserChallengeStatusEnum.name())
            .build();
    }

    public static List<UserChallengeStatusGet> getAllUserChallengeStatus(
        int numOfGeneratedElements, long firstId) {
        List<UserChallengeStatusGet> generatedUserChallengeStatusGetList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            String randomStatus = getRandomUserChallengeStatus();
            UserChallengeStatusGet generatedUserChallengeStatusGet =
                UserChallengeStatusGet.builder()
                    .id(firstId + i)
                    .statusName(randomStatus)
                    .build();
            generatedUserChallengeStatusGetList.add(generatedUserChallengeStatusGet);
        }
        return generatedUserChallengeStatusGetList;
    }

    public static List<UserChallengeStatusForOption> getAllUserChallengeStatusForOptions(
        List<UserChallengeStatusGet> userChallengeStatusGetList) {
        return userChallengeStatusGetList.stream()
            .map(element -> UserChallengeStatusForOption.builder()
                .label(element.getStatusName())
                .value(element.getStatusName())
                .build())
            .collect(Collectors.toList());
    }

    public static UserChallengeStatusAdd getUserChallengeStatusAdd(
        TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        return UserChallengeStatusAdd
            .builder()
            .statusName(testUserChallengeStatusEnum.name())
            .build();
    }

    public static UserChallengeStatusUpdate getUserChallengeStatusUpdate(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        return UserChallengeStatusUpdate
            .builder()
            .id(id)
            .statusName(testUserChallengeStatusEnum.name())
            .build();
    }

    public static UserChallengeStatusDelete getUserChallengeStatusDelete(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        return UserChallengeStatusDelete
            .builder()
            .id(id)
            .statusName(testUserChallengeStatusEnum.name())
            .build();
    }

    public static UserChallengeStatusArch getUserChallengeStatusArch(
        long id, TestUserChallengeStatusEnum testUserChallengeStatusEnum) {
        return UserChallengeStatusArch
            .builder()
            .id(id)
            .statusName(testUserChallengeStatusEnum.name())
            .build();
    }

    //ChallengeDurationService

    public static Challenge getChallenge(long challengeId, long userId, boolean isActive,
                                         long roleId, int numOfTasks, long firstTaskId, LocalDate firstStartDate) {
        return Challenge
            .builder()
            .id(challengeId)
            .name("name" + challengeId)
            .title("title" + challengeId)
            .description("description" + challengeId)
            .picture("picture" + challengeId)
            .sortNumber(challengeId)
            .registrationLink("registrationLink" + challengeId)
            .user(getUser(userId, roleId))
            .isActive(isActive)
            .tasks(getAllTask(numOfTasks, firstTaskId, firstStartDate))
            .build();
    }

    public static User getUser(long id, long roleId) {
        return User
            .builder()
            .id(id)
            .email("email" + id)
            .password("password" + id)
            .firstName("firstName" + id)
            .lastName("lastName" + id)
            .phone("phone" + id)
            .urlLogo("urlLogo" + id)
            .role(getRole((int) roleId))
            .providerId("providerId" + id)
            .verificationCode("verificationCode" + id)
            .build();
    }


    public static Role getRole(int id) {
        return Role
            .builder()
            .id(id)
            .name("name" + id)
            .build();
    }

    public static Task getTask(long id, LocalDate startDate) {
        return Task
            .builder()
            .id(id)
            .name("name" + id)
            .headerText("headerText" + id)
            .description("description" + id)
            .picture("picture" + id)
            .startDate(startDate)
            .build();
    }

    public static Set<Task> getAllTask(int numOfGeneratedElements, long firstId, LocalDate firstStartDate) {
        Set<Task> generatedTaskSet = new LinkedHashSet<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            long numOfIndex = firstId + i;
            Task generatedTask = Task.builder()
                .id(firstId + i)
                .name("name" + numOfIndex)
                .headerText("headerText" + numOfIndex)
                .description("description" + numOfIndex)
                .picture("picture" + numOfIndex)
                .startDate(firstStartDate.plusDays(i))
                .build();
            generatedTaskSet.add(generatedTask);
        }
        return generatedTaskSet;
    }

    public static List<ChallengeDurationForAdmin> getListChallengeDurationForAdmin(
        int numOfGeneratedElements, long firstId) {
        List<ChallengeDurationForAdmin> generatedChallengeDurationForAdminList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            long numOfIndex = firstId + i;
            ChallengeDurationForAdmin generatedChallengeDurationForAdmin = ChallengeDurationForAdmin.builder()
                .challengeId(firstId + i)
                .challengeName("challengeName" + numOfIndex)
                .isActive(getRandomBoolean())
                .build();
            generatedChallengeDurationForAdminList.add(generatedChallengeDurationForAdmin);
        }
        return generatedChallengeDurationForAdminList;
    }

    public static boolean getRandomBoolean() {
        return RANDOM.nextInt(2) == 1;
    }

    public static List<ChallengeDurationForAdminDurationLocalDate> getListChallengeDurationForAdminDurationLocalDate(
        int numOfGeneratedElements, LocalDate firstStartDate, LocalDate firstEndDate) {
        List<ChallengeDurationForAdminDurationLocalDate> generateChallengeDurationForAdminDurationLocalDateList =
            new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            ChallengeDurationForAdminDurationLocalDate generatedChallengeDurationForAdminDurationLocalDate =
                ChallengeDurationForAdminDurationLocalDate.builder()
                    .startDate(firstStartDate.plusDays(i))
                    .endDate(firstEndDate.plusDays(i))
                    .build();
            generateChallengeDurationForAdminDurationLocalDateList.add(
                generatedChallengeDurationForAdminDurationLocalDate);
        }
        return generateChallengeDurationForAdminDurationLocalDateList;
    }

    public static List<ChallengeDurationForAdminDurationString> getListChallengeDurationForAdminDurationString(
        int numOfGeneratedElements, LocalDate firstStartDate, LocalDate firstEndDate) {
        List<ChallengeDurationForAdminDurationString> generateChallengeDurationForAdminDurationStringList =
            new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            ChallengeDurationForAdminDurationString generatedChallengeDurationForAdminDurationString =
                ChallengeDurationForAdminDurationString.builder()
                    .startDate(firstStartDate.plusDays(i).toString())
                    .endDate(firstEndDate.plusDays(i).toString())
                    .build();
            generateChallengeDurationForAdminDurationStringList.add(
                generatedChallengeDurationForAdminDurationString);
        }
        return generateChallengeDurationForAdminDurationStringList;
    }

    public static Set<ChallengeDuration> getChallengeDurationSet(
        int numOfGeneratedElements, boolean userExist, long challengeId, long userId, boolean isActive,
        long roleId, int numOfTasks, long firstTaskId, LocalDate firstTaskStartDate, long durationId,
        LocalDate firstDurationStartDate, LocalDate firstDurationEndDate) {
        Set<ChallengeDuration> generateChallengeDurationSet = new LinkedHashSet<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            ChallengeDuration generatedChallengeDuration =
                ChallengeDuration.builder()
                    .id(0L)
                    .userExist(userExist)
                    .challenge(getChallenge(challengeId, userId, isActive, roleId, numOfTasks, firstTaskId,
                        firstTaskStartDate))
                    .durationEntity(getDurationEntity(durationId + i, firstDurationStartDate.plusDays(i),
                        firstDurationEndDate.plusDays(i)))
                    .build();
            generateChallengeDurationSet.add(generatedChallengeDuration);
        }
        return generateChallengeDurationSet;
    }

    public static Set<ChallengeDurationAdd> getChallengeDurationAddSet(
        int numOfGeneratedElements, LocalDate firstDurationStartDate, LocalDate firstDurationEndDate) {
        Set<ChallengeDurationAdd> generateChallengeDurationAddSet = new LinkedHashSet<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            ChallengeDurationAdd generatedChallengeDurationAdd =
                ChallengeDurationAdd.builder()
                    .startDate(firstDurationStartDate.plusDays(i))
                    .endDate(firstDurationEndDate.plusDays(i))
                    .build();
            generateChallengeDurationAddSet.add(generatedChallengeDurationAdd);
        }
        return generateChallengeDurationAddSet;
    }

    public static ChallengeDuration getChallengeDuration(long id, boolean userExist, Challenge challenge,
                                                         DurationEntity durationEntity) {
        return ChallengeDuration
            .builder()
            .id(id)
            .userExist(userExist)
            .challenge(challenge)
            .durationEntity(durationEntity)
            .build();
    }

    public static ChallengeDurationExistUserResponse getChallengeDurationExistUserResponseTrue() {
        return ChallengeDurationExistUserResponse
            .builder()
            .userExists(true)
            .build();
    }

    public static ChallengeDurationDelete getChallengeDurationDelete(
        long challengeId, LocalDate startDate, LocalDate endDate) {
        return ChallengeDurationDelete
            .builder()
            .challengeId(challengeId)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    public static ChallengeDurationDeleteResponse getChallengeDurationDeleteResponse(
        LocalDate startDate, LocalDate endDate) {
        return ChallengeDurationDeleteResponse
            .builder()
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    public static List<UserChallenge> getUserChallengeList(
        int numOfGeneratedElements, long userChallengeId, LocalDate registrationDate, User user,
        ChallengeDuration challengeDuration, UserChallengeStatus userChallengeStatus) {
        List<UserChallenge> generateUserChallengeList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            UserChallenge generatedUserChallenge =
                UserChallenge.builder()
                    .id(userChallengeId + i)
                    .registrationDate(registrationDate)
                    .user(user)
                    .challengeDuration(challengeDuration)
                    .userChallengeStatus(userChallengeStatus)
                    .build();
            generateUserChallengeList.add(generatedUserChallenge);
        }
        return generateUserChallengeList;
    }

    public static ChallengeDurationArch getChallengeDurationArch(
        long challengeDurationId, boolean userExist, long challengeId, long durationEntityId) {
        return ChallengeDurationArch
            .builder()
            .id(challengeDurationId)
            .userExist(userExist)
            .challengeId(challengeId)
            .durationEntityId(durationEntityId)
            .build();
    }

    public static ChallengeDurationForAdminDurationLocalDate getChallengeDurationForAdminDurationLocalDate(
        LocalDate startDate, LocalDate endDate) {
        return ChallengeDurationForAdminDurationLocalDate
            .builder()
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    //UserChallengeService

    public static List<UserChallengeForProfileGet> getUserChallengeForProfileGetList(
        int numOfGeneratedElements, long firstId, long firstChallengeId,
        LocalDate registrationChallengeDate, LocalDate startChallengeDate, LocalDate endChallengeDate) {
        List<UserChallengeForProfileGet> generatedUserChallengeForProfileGetList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            long numOfIndex = firstId + i;
                UserChallengeForProfileGet generatedUserChallengeForProfileGet =
                UserChallengeForProfileGet.builder()
                    .id(firstId + i)
                    .challengeId(firstChallengeId + i)
                    .challengeName("challengeName" + numOfIndex)
                    .registrationChallengeDate(registrationChallengeDate.plusDays(i))
                    .startChallengeDate(startChallengeDate.plusDays(i))
                    .endChallengeDate(endChallengeDate.plusDays(i))
                    .userChallengeStatus("userChallengeStatus" + numOfIndex)
                    .build();
            generatedUserChallengeForProfileGetList.add(generatedUserChallengeForProfileGet);
        }
        return generatedUserChallengeForProfileGetList;
    }

    public static UserChallenge getUserChallenge(
        long userChallengeId, User user, ChallengeDuration challengeDuration, UserChallengeStatus userChallengeStatus) {
        return UserChallenge.builder()
            .id(userChallengeId)
            .registrationDate(LocalDate.now())
            .user(user)
            .challengeDuration(challengeDuration)
            .userChallengeStatus(userChallengeStatus)
            .build();
    }

    public static ChallengeDuration getChallengeDuration(
        long challengeDurationId, boolean userExist, DurationEntity durationEntity, Challenge challenge) {
        return ChallengeDuration.builder()
            .id(challengeDurationId)
            .userExist(userExist)
            .durationEntity(durationEntity)
            .challenge(challenge)
            .build();
    }

//    public static DurationEntity getDurationEntity(long durationEntityId, LocalDate startDate, LocalDate endDate) {
//        return DurationEntity.builder()
//            .id(durationEntityId)
//            .startDate(startDate)
//            .endDate(endDate)
//            .build();
//    }

    public static Challenge getChallenge(long challengeId, String challengeName, boolean isChallengeActive) {
        return Challenge.builder()
            .id(challengeId)
            .name(challengeName)
            .isActive(isChallengeActive)
            .build();
    }

    public static UserChallengeForUserCreateWithDate getUserChallengeForUserCreateWithDate(
        long userId, long challengeId, LocalDate startDate, LocalDate endDate) {
        return UserChallengeForUserCreateWithDate.builder()
            .userId(userId)
            .challengeId(challengeId)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    public static UserChallengeCreateResponse getUserChallengeCreateResponse(
        String challengeName, LocalDate startDate, LocalDate endDate) {
        return UserChallengeCreateResponse.builder()
            .challengeName(challengeName)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }

    public static UserChallengeForProfileDelete getUserChallengeForProfileDelete(
        long userIdForDelete, long challengeIdForDelete, LocalDate startDate, LocalDate endDate) {
        return UserChallengeForProfileDelete.builder()
            .userIdForDelete(userIdForDelete)
            .challengeIdForDelete(challengeIdForDelete)
            .startChallengeDate(startDate)
            .endChallengeDate(endDate)
            .build();
    }

    public static UserChallengeDeleteResponse getUserChallengeDeleteResponse(
        String challengeName, LocalDate startDate, LocalDate endDate) {
        return UserChallengeDeleteResponse.builder()
            .challengeName(challengeName)
            .startChallengeDate(startDate)
            .endChallengeDate(endDate)
            .build();
    }

    public static List<UserChallengeForUserGetLocalDate> getUserChallengeForUserGetLocalDateList(
        int numOfGeneratedElements, LocalDate startDate, LocalDate endDate) {
        List<UserChallengeForUserGetLocalDate> generatedUserChallengeForUserGetLocalDateList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            UserChallengeForUserGetLocalDate generatedUserChallengeForUserGetLocalDate =
                UserChallengeForUserGetLocalDate.builder()
                    .startDate(startDate.plusDays(i))
                    .endDate(endDate.plusDays(i))
                    .build();
            generatedUserChallengeForUserGetLocalDateList.add(generatedUserChallengeForUserGetLocalDate);
        }
        return generatedUserChallengeForUserGetLocalDateList;
    }

    public static List<UserChallengeForUserGetString> getUserChallengeForUserGetStringList(
        int numOfGeneratedElements, LocalDate startDate, LocalDate endDate) {
        List<UserChallengeForUserGetString> generatedUserChallengeForUserGetStringList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            UserChallengeForUserGetString generatedUserChallengeForUserGetString =
                UserChallengeForUserGetString.builder()
                    .startDate(startDate.plusDays(i).toString())
                    .endDate(endDate.plusDays(i).toString())
                    .build();
            generatedUserChallengeForUserGetStringList.add(generatedUserChallengeForUserGetString);
        }
        return generatedUserChallengeForUserGetStringList;
    }

    public static List<UserChallengeForAdminGet> getUserChallengeForAdminGetList(
        int numOfGeneratedElements, long challengeId, String challengeName, boolean isActive) {
        List<UserChallengeForAdminGet> generatedUserChallengeForAdminGetList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            UserChallengeForAdminGet generatedUserChallengeForAdminGet =
                UserChallengeForAdminGet.builder()
                    .challengeId(challengeId + i)
                    .challengeName(challengeName + i)
                    .isActive(isActive)
                    .build();
            generatedUserChallengeForAdminGetList.add(generatedUserChallengeForAdminGet);
        }
        return generatedUserChallengeForAdminGetList;
    }

    public static List<UserChallengeForAdminGetChallengeDuration> getUserChallengeForAdminGetChallengeDurationList(
        int numOfGeneratedElements, long challengeId, long durationId, boolean userExist,
        LocalDate startDate, LocalDate endDate) {
        List<UserChallengeForAdminGetChallengeDuration> generatedUserChallengeForAdminGetChallengeDurationList =
            new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            UserChallengeForAdminGetChallengeDuration generatedUserChallengeForAdminGetChallengeDuration =
                UserChallengeForAdminGetChallengeDuration.builder()
                    .challengeId(challengeId + i)
                    .durationId(durationId + i)
                    .userExist(userExist)
                    .startDate(startDate.plusDays(i))
                    .endDate(endDate.plusDays(i))
                    .build();
            generatedUserChallengeForAdminGetChallengeDurationList.add(
                generatedUserChallengeForAdminGetChallengeDuration);
        }
        return generatedUserChallengeForAdminGetChallengeDurationList;
    }

    public static List<UserChallengeForAdminRegisteredUser> getUserChallengeForAdminRegisteredUserList(
        int numOfGeneratedElements, long userChallengeId, long userId, LocalDate registrationChallengeDate) {
        List<UserChallengeForAdminRegisteredUser> generatedUserChallengeForAdminRegisteredUserList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            long numOfIndex = userChallengeId + i;
            UserChallengeForAdminRegisteredUser generatedUserChallengeForAdminRegisteredUser =
                UserChallengeForAdminRegisteredUser.builder()
                    .userChallengeId(userChallengeId + i)
                    .userId(userId + i)
                    .firstName("firstName" + numOfIndex)
                    .lastName("lastName" + numOfIndex)
                    .email("email" + numOfIndex)
                    .phone("phone" + numOfIndex)
                    .roleName("roleName" + numOfIndex)
                    .userChallengeStatus("userChallengeStatus" + numOfIndex)
                    .registrationChallengeDate(registrationChallengeDate)
                    .build();
            generatedUserChallengeForAdminRegisteredUserList.add(generatedUserChallengeForAdminRegisteredUser);
        }
        return generatedUserChallengeForAdminRegisteredUserList;
    }

    public static UserChallengeForAdminGetByChallengeIdDurationId getUserChallengeForAdminGetByChallengeIdDurationId(
        long challengeId, long durationId) {
        return UserChallengeForAdminGetByChallengeIdDurationId.builder()
            .challengeId(challengeId)
            .durationId(durationId)
            .build();
    }

    public static List<UserChallengeForAdminNotRegisteredUser> getUserChallengeForAdminNotRegisteredUserList(
        int numOfGeneratedElements, long userId) {
        List<UserChallengeForAdminNotRegisteredUser> generatedUserChallengeForAdminNotRegisteredUserList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            long numOfIndex = userId + i;
            UserChallengeForAdminNotRegisteredUser generatedUserChallengeForAdminNotRegisteredUser =
                UserChallengeForAdminNotRegisteredUser.builder()
                    .userId(numOfIndex)
                    .firstName("firstName" + numOfIndex)
                    .lastName("lastName" + numOfIndex)
                    .email("email" + numOfIndex)
                    .phone("phone" + numOfIndex)
                    .roleName("roleName" + numOfIndex)
                    .build();
            generatedUserChallengeForAdminNotRegisteredUserList.add(generatedUserChallengeForAdminNotRegisteredUser);
        }
        return generatedUserChallengeForAdminNotRegisteredUserList;
    }

    public static UserChallengeForUserCreate getUserChallengeForUserCreate(
        long userId, long challengeId, long durationId) {
        return UserChallengeForUserCreate.builder()
            .userId(userId)
            .challengeId(challengeId)
            .durationId(durationId)
            .build();
    }

    public static List<UserChallengeForExist> getUserChallengeForExistList(
        int numOfGeneratedElements, long userId, long challengeDurationId) {
        List<UserChallengeForExist> generatedUserChallengeForExistList = new ArrayList<>();
        for (int i = 0; i < numOfGeneratedElements; i++) {
            UserChallengeForExist generatedUserChallengeForExist =
                UserChallengeForExist.builder()
                    .userId(userId + i)
                    .challengeDurationId(challengeDurationId + i)
                    .build();
            generatedUserChallengeForExistList.add(generatedUserChallengeForExist);
        }
        return generatedUserChallengeForExistList;
    }

    public static UserChallengeForAdminUpdate getUserChallengeForAdminUpdate(
        long userChallengeId, long userId) {
        return UserChallengeForAdminUpdate.builder()
            .userChallengeId(userChallengeId)
            .userId(userId)
            .firstName("firstName")
            .lastName("lastName")
            .email("email")
            .phone("phone")
            .statusName("statusName")
            .build();
    }

    public static UserChallengeUpdateResponse getUserChallengeUpdateResponse(
        long userChallengeId, String challengeName, LocalDate startChallengeDate, LocalDate endChallengeDate) {
        return UserChallengeUpdateResponse.builder()
            .userChallengeId(userChallengeId)
            .challengeName(challengeName)
            .startChallengeDate(startChallengeDate)
            .endChallengeDate(endChallengeDate)
            .build();
    }

    public static UserChallengeForAdminDelete getUserChallengeForAdminDelete(
        long userId, long challengeId, long durationId) {
        return UserChallengeForAdminDelete.builder()
            .userId(userId)
            .challengeId(challengeId)
            .durationId(durationId)
            .build();
    }
    public static UserChallengeArch getUserChallengeArch(long userChallengeId, LocalDate registrationDate, long userId,
                                                         long challengeDurationId, long userChallengeStatusId) {
        return UserChallengeArch
            .builder()
            .id(userChallengeId)
            .registrationDate(registrationDate)
            .userId(userId)
            .challengeDurationId(challengeDurationId)
            .userChallengeStatusId(userChallengeStatusId)
            .build();
    }

}
