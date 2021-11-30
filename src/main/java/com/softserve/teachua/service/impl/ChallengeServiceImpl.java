package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge.*;
import com.softserve.teachua.dto.task.TaskNameProfile;
import com.softserve.teachua.dto.task.TaskPreview;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.repository.ChallengeRepository;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.service.TaskService;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.utils.HtmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final DtoConverter dtoConverter;
    private final UserService userService;
    private final ArchiveService archiveService;
    private final TaskRepository taskRepository;
    private TaskService taskService;

    @Autowired
    public ChallengeServiceImpl(
            ChallengeRepository challengeRepository,
            DtoConverter dtoConverter,
            UserService userService,
            ArchiveService archiveService,
            TaskRepository taskRepository
    ) {
        this.challengeRepository = challengeRepository;
        this.dtoConverter = dtoConverter;
        this.userService = userService;
        this.archiveService = archiveService;
        this.taskRepository = taskRepository;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public List<ChallengePreview> getAllChallenges(Boolean active) {
        List<ChallengePreview> resultList = new LinkedList<>();
        List<Challenge> list;
        list = active != null
                ? challengeRepository.getByIsActiveOrderBySortNumberDesc(active)
                : challengeRepository.findAll(Sort.by(Sort.Direction.DESC, "sortNumber"));
        list.forEach((challenge ->
                resultList.add(dtoConverter.convertToDto(challenge, ChallengePreview.class))));
        return resultList;
    }

    @Override
    public Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format("Challenge not found by id: %s", id)));
    }

    @Override
    public SuccessCreatedChallenge createChallenge(
            CreateChallenge createChallenge) {
        HtmlUtils.validateDescription(createChallenge.getDescription());
        Challenge challenge = dtoConverter.convertToEntity(createChallenge, new Challenge());
        challenge.setUser(userService.getCurrentUser());
        challenge.setIsActive(true);
        return dtoConverter.convertToDto(challengeRepository.save(challenge), SuccessCreatedChallenge.class);
    }

    @Override
    public SuccessUpdatedChallenge updateChallenge(Long id, UpdateChallenge updateChallenge) {
        HtmlUtils.validateDescription(updateChallenge.getDescription());
        Challenge challenge = getChallengeById(id);
        BeanUtils.copyProperties(updateChallenge, challenge);
        return dtoConverter.convertToDto(challengeRepository.save(challenge), SuccessUpdatedChallenge.class);
    }

    @Override
    public ChallengeDeleteResponse deleteChallenge(Long id) {
        Challenge challenge = getChallengeById(id);
        ChallengeDeleteResponse challengeResponse =
                dtoConverter.convertToDto(challenge, ChallengeDeleteResponse.class);
        challengeResponse.setTasks(new HashSet<>());
        Set<Task> taskSet = challenge.getTasks();
        challenge.setTasks(null);
//        archiveService.saveModel(challenge);
        taskSet.forEach((task) ->
                challengeResponse.getTasks().add(
                        dtoConverter.convertFromDtoToDto(
                                taskService.deleteTask(task.getId()),
                                new TaskNameProfile())));
        challengeRepository.deleteById(id);
        challengeRepository.flush();
        return challengeResponse;
    }

    @Override
    public ChallengeProfile getChallenge(Long id) {
        Challenge challenge = getChallengeById(id);
        if (!challenge.getIsActive()) {
            userService.verifyIsUserAdmin();
        }
        ChallengeProfile challengeProfile =
                dtoConverter.convertToDto(challenge, ChallengeProfile.class);
        Function<Task, TaskPreview> function = (task) -> dtoConverter.convertToDto(task, TaskPreview.class);
        List<TaskPreview> tasks = taskRepository
                .findTasksByChallengeAndStartDateBeforeOrderByStartDate(challenge, LocalDate.now().plusDays(1))
                .stream().map(function).collect(Collectors.toList());
        challengeProfile.setTasks(tasks);
        return challengeProfile;
    }


    @Override
    public SuccessUpdateChallengePreview updateChallengePreview(Long id,
                                                                SuccessUpdateChallengePreview updateChallengePreview) {
        Challenge challenge = getChallengeById(id);
        BeanUtils.copyProperties(updateChallengePreview, challenge);
        return dtoConverter.convertToDto(challengeRepository.save(challenge), SuccessUpdateChallengePreview.class);
    }
}
