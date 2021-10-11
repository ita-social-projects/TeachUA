package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge.*;
import com.softserve.teachua.dto.task.TaskNameProfile;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.repository.ChallengeRepository;
import com.softserve.teachua.service.*;
import com.softserve.teachua.utils.HtmlValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final DtoConverter dtoConverter;
    private final UserService userService;
    private final ArchiveService archiveService;
    private final FileUploadService fileUploadService;
    private TaskService taskService;

    @Autowired
    public ChallengeServiceImpl(
            ChallengeRepository challengeRepository,
            DtoConverter dtoConverter,
            UserService userService,
            ArchiveService archiveService,
            FileUploadService fileUploadService
    ) {
        this.challengeRepository = challengeRepository;
        this.dtoConverter = dtoConverter;
        this.userService = userService;
        this.archiveService = archiveService;
        this.fileUploadService = fileUploadService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public List<ChallengePreview> getAllChallenges(Boolean isActive) {
        List<ChallengePreview> resultList = new LinkedList<>();
        List<Challenge> list;
        list = isActive != null
                ? challengeRepository.getByIsActiveOrderBySortIdDesc(isActive)
                : challengeRepository.findAll(Sort.by(Sort.Direction.DESC, "sortId"));
        list.forEach((challenge ->
                resultList.add(dtoConverter.convertToDto(challenge, ChallengePreview.class))));
        return resultList;
    }

    @Override
    public Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(()
                        -> {
                    throw new NotExistException(String.format("Challenge not found by id: %s", id));
                });
    }

    @Override
    public SuccessCreatedChallenge createChallenge(
            CreateChallenge createChallenge,
            HttpServletRequest httpServletRequest) {
        HtmlValidator.validateDescription(createChallenge.getDescription());
        Challenge challenge = dtoConverter.convertToEntity(createChallenge, new Challenge());
        challenge.setUser(userService.getUserFromRequest(httpServletRequest));
        challenge.setIsActive(true);
        return dtoConverter.convertToDto(challengeRepository.save(challenge), SuccessCreatedChallenge.class);
    }

    @Override
    public SuccessUpdatedChallenge updateChallenge(Long id, UpdateChallenge updateChallenge) {
        HtmlValidator.validateDescription(updateChallenge.getDescription());
        Challenge challenge = getChallengeById(id);
        if (!challenge.getPicture().equals(updateChallenge.getPicture())) {
            fileUploadService.deleteFile(challenge.getPicture());
        }
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
        archiveService.saveModel(challenge);
        taskSet.forEach((task) ->
                challengeResponse.getTasks().add(
                        dtoConverter.convertFromDtoToDto(
                                taskService.deleteTask(task.getId()),
                                new TaskNameProfile())));
        fileUploadService.deleteFile(challenge.getPicture());
        challengeRepository.deleteById(id);
        challengeRepository.flush();
        return challengeResponse;
    }

    @Override
    public ChallengeProfile getChallenge(Long id) {
        Challenge challenge = getChallengeById(id);
        ChallengeProfile challengeProfile =
                dtoConverter.convertToDto(challenge, ChallengeProfile.class);
        challengeProfile.setTasks(new PageImpl<>(
                taskService.getTasksByChallengeId(id)
                        .stream()
                        .filter(task -> task.getStartDate().isAfter(LocalDate.now().minusDays(1)))
                        .collect(Collectors.toList())));
        return challengeProfile;
    }
}
