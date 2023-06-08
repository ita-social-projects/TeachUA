package com.softserve.teachua.service.impl;

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
import com.softserve.teachua.dto.task.TaskPreview;
import com.softserve.teachua.dto.task.UpdateTask;
import com.softserve.commons.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.model.archivable.ChallengeArch;
import com.softserve.teachua.repository.ChallengeRepository;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.ArchiveMark;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.TaskService;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.utils.ChallengeUtil;
import com.softserve.teachua.utils.HtmlUtils;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ChallengeServiceImpl implements ChallengeService, ArchiveMark<Challenge> {
    private final ChallengeRepository challengeRepository;
    private final DtoConverter dtoConverter;
    private final UserService userService;
    private final ArchiveService archiveService;
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    private TaskService taskService;

    @Autowired
    public ChallengeServiceImpl(ChallengeRepository challengeRepository, DtoConverter dtoConverter,
                                UserService userService, ArchiveService archiveService, TaskRepository taskRepository,
                                ObjectMapper objectMapper,TaskService taskService) {
        this.challengeRepository = challengeRepository;
        this.dtoConverter = dtoConverter;
        this.userService = userService;
        this.archiveService = archiveService;
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
        this.taskService = taskService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public List<ChallengePreview> getAllChallenges(Boolean active) {
        List<ChallengePreview> resultList = new LinkedList<>();
        List<Challenge> list;
        list = active != null ? challengeRepository.getByIsActiveOrderBySortNumberDesc(active)
                : challengeRepository.findAll(Sort.by(Sort.Direction.DESC, "sortNumber"));
        list.forEach((challenge -> resultList.add(dtoConverter.convertToDto(challenge, ChallengePreview.class))));
        return resultList;
    }

    @Override
    public Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format("Challenge not found by id: %s", id)));
    }

    @Override
    public SuccessCreatedChallenge createChallenge(CreateChallenge createChallenge) {
        HtmlUtils.validateDescription(createChallenge.getDescription());
        validateSortNumber(createChallenge.getSortNumber());
        Challenge challenge = dtoConverter.convertToEntity(createChallenge, new Challenge());
        challenge.setUser(userService.getAuthenticatedUser());
        challenge.setIsActive(true);
        return dtoConverter.convertToDto(challengeRepository.save(challenge), SuccessCreatedChallenge.class);
    }

    @Override
    public SuccessUpdatedChallenge updateChallenge(Long id, UpdateChallenge updateChallenge) {
        HtmlUtils.validateDescription(updateChallenge.getDescription());
        if (!challengeRepository.getReferenceById(id).getSortNumber().equals(updateChallenge.getSortNumber())) {
            validateSortNumber(updateChallenge.getSortNumber());
        }
        Challenge challenge = getChallengeById(id);
        BeanUtils.copyProperties(updateChallenge, challenge);
        return dtoConverter.convertToDto(challengeRepository.save(challenge), SuccessUpdatedChallenge.class);
    }

    @Override
    public ChallengeDeleteResponse deleteChallenge(Long id) {
        Challenge challenge = getChallengeById(id);
        challenge.getTasks().forEach(task -> {
            task.setChallenge(null);
            taskRepository.save(task);
        });
        challengeRepository.deleteById(id);
        challengeRepository.flush();
        archiveModel(challenge);
        return dtoConverter.convertToDto(challenge, ChallengeDeleteResponse.class);
    }

    @Override
    public ChallengeProfile getChallenge(Long id) {
        Challenge challenge = getChallengeById(id);
        if (Boolean.FALSE.equals(challenge.getIsActive())) {
            userService.verifyIsUserAdmin();
        }
        ChallengeProfile challengeProfile = dtoConverter.convertToDto(challenge, ChallengeProfile.class);
        Function<Task, TaskPreview> function = task -> dtoConverter.convertToDto(task, TaskPreview.class);
        List<TaskPreview> tasks = taskRepository.findCurrentTasksByChallenge(challenge)
                .stream().map(function).toList();
        challengeProfile.setTasks(tasks);
        return challengeProfile;
    }

    @Override
    public SuccessUpdateChallengePreview updateChallengePreview(Long id,
            SuccessUpdateChallengePreview updateChallengePreview) {
        Challenge challenge = getChallengeById(id);
        if (!challengeRepository.getReferenceById(id).getSortNumber().equals(updateChallengePreview.getSortNumber())) {
            validateSortNumber(updateChallengePreview.getSortNumber());
        }
        BeanUtils.copyProperties(updateChallengePreview, challenge);
        return dtoConverter.convertToDto(challengeRepository.save(challenge), SuccessUpdateChallengePreview.class);
    }

    @Override
    public void archiveModel(Challenge challenge) {
        ChallengeArch challengeArch = dtoConverter.convertToDto(challenge, ChallengeArch.class);
        challengeArch.setTasksIds(challenge.getTasks().stream().map(Task::getId).collect(Collectors.toSet()));
        archiveService.saveModel(challengeArch);
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        ChallengeArch challengeArch = objectMapper.readValue(archiveObject, ChallengeArch.class);
        Challenge challenge = Challenge.builder().build();
        challenge = dtoConverter.convertToEntity(challengeArch, challenge).withId(null);
        if (Optional.ofNullable(challengeArch.getUserId()).isPresent()) {
            challenge.setUser(userService.getUserById(challengeArch.getUserId()));
        }
        Challenge finalChallenge = challengeRepository.save(challenge);
        challengeArch.getTasksIds().stream().map(taskService::getTaskById)
                .forEach(task -> task.setChallenge(finalChallenge));
    }

    @Override
    public Challenge getChallengeByName(String name) {
        return challengeRepository.findByName(name).stream().findAny()
                .orElseThrow(() -> new NotExistException(String.format("Challenge not found by name: %s", name)));
    }

    private void validateSortNumber(Long sortNumber) {
        List<Long> unavailableSortNumbers = challengeRepository.findAll().stream().map(Challenge::getSortNumber)
                .toList();
        ChallengeUtil.validateSortNumber(sortNumber, unavailableSortNumbers);
    }

    @Override
    public List<SuccessUpdatedTask> cloneChallenge(Long id, UpdateChallengeDate startDate) {
        Challenge challenge = getChallengeById(id);
        List<Task> tasks = new ArrayList<>(challenge.getTasks()).stream()
                .sorted(Comparator.comparing(Task::getStartDate)).toList();
        List<SuccessUpdatedTask> updatedTasks = new ArrayList<>();
        long daysBetween = DAYS.between(tasks.get(0).getStartDate(), startDate.getStartDate());
        for (Task task : tasks) {
            LocalDate currentStartDate = task.getStartDate();
            task.setStartDate(currentStartDate.plusDays(daysBetween));
            UpdateTask updateTask = dtoConverter.convertFromDtoToDto(task, new UpdateTask());
            updatedTasks.add(taskService.updateTask(task.getId(), updateTask));
        }
        return updatedTasks;
    }
}
