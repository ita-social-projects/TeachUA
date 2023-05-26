package com.softserve.teachua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge.ChallengeDeleteResponse;
import com.softserve.teachua.dto.challenge.ChallengePreview;
import com.softserve.teachua.dto.challenge.ChallengeProfile;
import com.softserve.teachua.dto.challenge.CreateChallenge;
import com.softserve.teachua.dto.challenge.SuccessCreatedChallenge;
import com.softserve.teachua.dto.challenge.SuccessUpdateChallengePreview;
import com.softserve.teachua.dto.challenge.SuccessUpdatedChallenge;
import com.softserve.teachua.dto.challenge.UpdateChallenge;
import com.softserve.teachua.dto.challenge.UpdateChallengeDate;
import com.softserve.teachua.dto.task.SuccessUpdatedTask;
import com.softserve.teachua.dto.task.UpdateTask;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Role;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.ChallengeArch;
import com.softserve.teachua.repository.ChallengeRepository;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.impl.ChallengeServiceImpl;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    private static final Integer ROLE_ID = 72;
    private static final String ROLE_NAME = "ADMIN";

    private static final String USER_EMAIL = "someuser@mail.com";
    private static final long USER_ID = 3L;
    private static final boolean IS_STATUS = true;
    private static final String PASSWORD = "12345";


    private static final Long CHALLENGE_ID = 2L;
    private static final Long CHALLENGE_WRONG_ID = 100L;

    private static final String CHALLENGE_NAME = "Назва Челенджу";
    private static final String UPDATED_NAME = "Зміненна Назва Челеджу";
    private static final String CHALLENGE_WRONG_NAME = "Помилкова назва Челенджу";

    private static final String CHALLENGE_TITLE = "Назва Челенджу";
    private static final String UPDATED_TITLE = "Змінений заголовок Челенджу";

    private static final String CHALLENGE_DESCRIPTION = "Опис Челенджу";
    private static final String UPDATED_DESCRIPTION = "Змінений опис Челенджу";

    private static final String CHALLENGE_PICTURE_PATH = "/upload/challenges/IMG_20221102_003151_584.jpg";
    private static final Long CHALLENGE_SORT_NUMBER = 12334L;
    private static final Boolean CHALLENGE_IS_ACTIVE = Boolean.TRUE;

    private static final Long CORRECT_TASK_ID = 1L;
    private static final String CORRECT_TASK_NAME = "Завдання до Челенджу";

    private final ModelMapper mapper = new ModelMapper();

    @Mock
    private ChallengeRepository challengeRepository;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private UserService userService;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private ObjectMapper objectMapper;
    @Spy
    @InjectMocks
    private ChallengeServiceImpl challengeService;

    private Challenge challenge;
    private Task task;
    private Role role;
    private User user;
    private final Set<Task> tasks = new HashSet<>();
    private ChallengePreview challengePreview;
    private ChallengeProfile challengeProfile;
    private ChallengeArch challengeArch;
    private SuccessUpdateChallengePreview successUpdateChallengePreview;
    private SuccessUpdatedChallenge successUpdatedChallenge;
    private SuccessCreatedChallenge successCreatedChallenge;
    private SuccessUpdatedTask successUpdatedTask;
    private ChallengeDeleteResponse challengeDeleteResponse;
    private CreateChallenge createChallenge;
    private UpdateChallenge updateChallenge;
    private UpdateChallengeDate updateChallengeDate;
    private UpdateTask updateTask;
    private SuccessUpdateChallengePreview challengeUpdatePreview;

    @BeforeEach
    void setMocks() {
        challenge = getChallenge();
        task = getTestTask();

        role = getTestRole();
        user = User.builder().id(USER_ID).email(USER_EMAIL).status(IS_STATUS).password(PASSWORD).role(role)
            .build();

        challenge.setUser(user);

        tasks.add(task);

        challenge.setTasks(tasks);

        updateChallengeDate = new UpdateChallengeDate();

        updateChallengeDate.setStartDate(LocalDate.now());

        createChallenge = mapper.map(challenge, CreateChallenge.class);
        updateChallenge = mapper.map(challenge, UpdateChallenge.class);
        challengePreview = mapper.map(challenge, ChallengePreview.class);
        challengeProfile = mapper.map(challenge, ChallengeProfile.class);
        challengeArch = mapper.map(challenge, ChallengeArch.class);
        challengeUpdatePreview = mapper.map(challenge, SuccessUpdateChallengePreview.class);

        successCreatedChallenge = mapper.map(challenge, SuccessCreatedChallenge.class);
        successUpdatedChallenge = mapper.map(challenge, SuccessUpdatedChallenge.class);
        successUpdatedTask = mapper.map(task, SuccessUpdatedTask.class);
        successUpdateChallengePreview = mapper.map(challenge, SuccessUpdateChallengePreview.class);
        challengeDeleteResponse = mapper.map(challenge, ChallengeDeleteResponse.class);

        updateTask = mapper.map(task, UpdateTask.class);


    }

    @Test
    @DisplayName("getListOfChallengesByIsActiveOrder should return List of ChallengePreview")
    void getListOfChallengesByIsActiveOrder() {
        when(challengeRepository.getByIsActiveOrderBySortNumberDesc(true))
            .thenReturn(Collections.singletonList(challenge));

        when(dtoConverter.convertToDto(challenge, ChallengePreview.class))
            .thenReturn(challengePreview);

        assertThat(challengeService.getAllChallenges(true))
            .isNotNull()
            .isEqualTo(Collections.singletonList(challengePreview));
    }

    @Test
    @DisplayName("getListOfChallengesBySortNumberDesc should return List of ChallengePreview")
    void getListOfChallengesBySortNumberDesc() {
        when(challengeRepository.findAll(Sort.by(Sort.Direction.DESC, "sortNumber")))
            .thenReturn(Collections.singletonList(challenge));

        when(dtoConverter.convertToDto(challenge, ChallengePreview.class))
            .thenReturn(challengePreview);

        assertThat(challengeService.getAllChallenges(null))
            .isNotNull()
            .isEqualTo(Collections.singletonList(challengePreview));
    }

    @Test
    @DisplayName("Should save Challenge in database and return SuccessCreatedChallenge")
    void addChallenge() {
        when(dtoConverter.convertToEntity(createChallenge, new Challenge())).thenReturn(challenge);
        when(challengeRepository.save(challenge)).thenReturn(challenge);
        when(dtoConverter.convertToDto(challenge, SuccessCreatedChallenge.class)).thenReturn(successCreatedChallenge);

        assertThat(challengeService.createChallenge(createChallenge)).isEqualTo(successCreatedChallenge);
    }

    @Test
    @DisplayName("getChallengeById should return Challenge")
    void getChallengeById() {
        when(challengeRepository.findById(challenge.getId()))
            .thenReturn(Optional.of(challenge));

        assertThat(challengeService.getChallengeById(challenge.getId()))
            .isNotNull()
            .isEqualTo(challenge);
    }

    @Test
    @DisplayName("getChallengeByWrongId should throw NotExistException")
    void getChallengeByWrongId() {
        assertThatThrownBy(()-> challengeService.getChallengeById(CHALLENGE_WRONG_ID)).isInstanceOf(NotExistException.class);
       verify(challengeRepository).findById(CHALLENGE_WRONG_ID);
    }

    @Test
    @DisplayName("getChallengeByName should return Challenge")
    void getChallengeByName() {
        when(challengeRepository.findByName(challenge.getName()))
            .thenReturn(Collections.singletonList(challenge));

        assertThat(challengeService.getChallengeByName(challenge.getName()))
            .isNotNull()
            .isEqualTo(challenge);
    }

    @Test
    @DisplayName("getChallengeByWrongName should throw NotExistException")
    void getChallengeByWrongName() {
        assertThatThrownBy(()-> challengeService.getChallengeByName(CHALLENGE_WRONG_NAME)).isInstanceOf(NotExistException.class);
        verify(challengeRepository).findByName(CHALLENGE_WRONG_NAME);
    }

    @Test
    @DisplayName("getChallengeTest should return ChallengeProfile")
    void getChallengeProfile() {
        when(challengeRepository.findById(challenge.getId())).thenReturn(Optional.of(challenge));
        when(dtoConverter.convertToDto(challenge, ChallengeProfile.class)).thenReturn(challengeProfile);

        assertThat(challengeService.getChallenge(challenge.getId())).isEqualTo(challengeProfile);
    }

    @Test
    @DisplayName("getChallengeProfileByNonActive should return ChallengeProfile by Challenge with isActive = false")
    void getChallengeProfileByNonActive() {
        challenge.setIsActive(false);

        when(challengeRepository.findById(challenge.getId())).thenReturn(Optional.of(challenge));
        when(dtoConverter.convertToDto(challenge, ChallengeProfile.class)).thenReturn(challengeProfile);

        assertThat(challengeService.getChallenge(challenge.getId())).isEqualTo(challengeProfile);
    }

    @Test
    @DisplayName("getChallengeProfileByWrongId should throw NotExistException")
    void getChallengeProfileByWrongId() {
        assertThatThrownBy(()-> challengeService.getChallenge(CHALLENGE_WRONG_ID)).isInstanceOf(NotExistException.class);
        verify(challengeService).getChallenge(CHALLENGE_WRONG_ID);
    }

    @Test
    @DisplayName("updateChallenge should return SuccessUpdatedChallenge")
    void updateChallenge() {
        when(challengeRepository.findById(challenge.getId()))
            .thenReturn(Optional.of(challenge));

        when(challengeRepository.save(challenge))
            .thenReturn(challenge);

        when(dtoConverter.convertToDto(challenge, SuccessUpdatedChallenge.class))
            .thenReturn(successUpdatedChallenge);

        when(challengeRepository.getReferenceById(challenge.getId()))
                .thenReturn(challenge);

        assertThat(challengeService.updateChallenge(challenge.getId(), updateChallenge))
            .isEqualTo(successUpdatedChallenge);

    }

    @Test
    @DisplayName("updateChallengeWithNewNameAndDescriptionAndTitle should save Challenge with updated Name, Title and Description")
    void updateChallengeWithNewNameAndDescriptionAndTitle() {
        when(challengeRepository.findById(challenge.getId()))
            .thenReturn(Optional.of(challenge));
        when(challengeRepository.getReferenceById(challenge.getId()))
                .thenReturn(challenge);

        updateChallenge.setDescription(UPDATED_DESCRIPTION);
        updateChallenge.setName(UPDATED_NAME);
        updateChallenge.setTitle(UPDATED_TITLE);

        challengeService.updateChallenge(challenge.getId(),updateChallenge);

        ArgumentCaptor<Challenge> savedChallengeCaptor = ArgumentCaptor.forClass(Challenge.class);
        verify(challengeRepository,times(1)).save(savedChallengeCaptor.capture());
        Challenge savedChallenge = savedChallengeCaptor.getValue();

        assertEquals(UPDATED_TITLE,savedChallenge.getTitle());
        assertEquals(UPDATED_NAME,savedChallenge.getName());
        assertEquals(UPDATED_DESCRIPTION,savedChallenge.getDescription());

    }

    @Test
    @DisplayName("cloneChallenge should return lost of SuccessUpdatedTask")
    void cloneChallenge() {
        when(challengeRepository.findById(challenge.getId()))
            .thenReturn(Optional.of(challenge));
        when(dtoConverter.convertFromDtoToDto(task, new UpdateTask()))
            .thenReturn(updateTask);
        when(taskService.updateTask(task.getId(), updateTask)).thenReturn(successUpdatedTask);


        assertThat(challengeService.cloneChallenge(challenge.getId(), updateChallengeDate))
            .isEqualTo(Collections.singletonList(successUpdatedTask));

    }

    @Test
    @DisplayName("updateChallengePreview should return SuccessUpdateChallengePreview")
    void updateChallengePreview() {
        when(challengeRepository.findById(challenge.getId()))
            .thenReturn(Optional.of(challenge));

        when(challengeRepository.save(challenge))
            .thenReturn(challenge);

        when(dtoConverter.convertToDto(challenge, SuccessUpdateChallengePreview.class))
            .thenReturn(successUpdateChallengePreview);

        when(challengeRepository.getReferenceById(challenge.getId()))
                .thenReturn(challenge);

        assertThat(challengeService.updateChallengePreview(challenge.getId(), challengeUpdatePreview))
            .isEqualTo(successUpdateChallengePreview);

    }

    @Test
    @DisplayName("updateChallengePreviewWithNewTitleAndName should save ChallengePreview with updated Name and Title")
    void updateChallengePreviewWithNewTitleAndName() {
        successUpdateChallengePreview.setName(UPDATED_NAME);
        successUpdateChallengePreview.setTitle(UPDATED_TITLE);

        when(challengeRepository.findById(challenge.getId()))
            .thenReturn(Optional.of(challenge));

        when(challengeRepository.getReferenceById(challenge.getId()))
                .thenReturn(challenge);

        challengeService.updateChallengePreview(challenge.getId(),successUpdateChallengePreview);

        ArgumentCaptor<Challenge> savedChallengeCaptor = ArgumentCaptor.forClass(Challenge.class);
        verify(challengeRepository,times(1)).save(savedChallengeCaptor.capture());

        Challenge savedChallenge = savedChallengeCaptor.getValue();
        assertEquals(UPDATED_NAME,savedChallenge.getName());
        assertEquals(UPDATED_TITLE,savedChallenge.getTitle());
    }

    @Test
    @DisplayName("deleteChallenge should return ChallengeDeleteResponse")
    void deleteChallenge() {
        when(challengeRepository.findById(challenge.getId()))
            .thenReturn(Optional.of(challenge));

        when(dtoConverter.convertToDto(challenge, ChallengeDeleteResponse.class))
            .thenReturn(challengeDeleteResponse);
        when(dtoConverter.convertToDto(challenge, ChallengeArch.class))
            .thenReturn(challengeArch);

        doNothing().when(challengeRepository).deleteById(anyLong());
        doNothing().when(challengeRepository).flush();

        assertThat(challengeService.deleteChallenge(challenge.getId()))
            .isEqualTo(challengeDeleteResponse);
    }

    @Test
    @DisplayName("Should set TaskService in Challenge")
    void setTaskService() {
        challengeService.setTaskService(taskService);
        verify(challengeService).setTaskService(taskService);
    }

    @Test
    @DisplayName("Should read ChallengeArch from JSON string, convert it to Challenge and save in database")
    void restoreChallengeModel() throws JsonProcessingException {
        when(objectMapper.readValue(anyString(), eq(ChallengeArch.class)))
            .thenReturn(challengeArch);
        when(dtoConverter.convertToEntity(eq(challengeArch), any(Challenge.class)))
            .thenReturn(challenge);
        when(userService.getUserById(challengeArch.getUserId())).thenReturn(user);

        when(taskService.getTaskById(task.getId())).thenReturn(task);

        challengeArch.setTasksIds(challenge.getTasks().stream().map(Task::getId).collect(Collectors.toSet()));

        challengeService.restoreModel(anyString());

        verify(challengeRepository).save(any(Challenge.class));
    }

    private Challenge getChallenge() {
        return Challenge.builder()
            .id(CHALLENGE_ID)
            .name(CHALLENGE_NAME)
            .title(CHALLENGE_TITLE)
            .description(CHALLENGE_DESCRIPTION)
            .picture(CHALLENGE_PICTURE_PATH)
            .sortNumber(CHALLENGE_SORT_NUMBER)
            .isActive(CHALLENGE_IS_ACTIVE)
            .build();
    }

    private Task getTestTask(){
        return Task.builder()
            .id(CORRECT_TASK_ID)
            .name(CORRECT_TASK_NAME)
            .challenge(challenge)
            .startDate(LocalDate.now())
            .build();
    }

    private Role getTestRole(){
        return Role.builder()
            .id(ROLE_ID)
            .name(ROLE_NAME)
            .build();
    }

}
