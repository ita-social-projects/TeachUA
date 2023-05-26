package com.softserve.teachua.tools.service.transfer_impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.task.SuccessCreatedTask;
import com.softserve.teachua.dto.task.TaskProfile;
import com.softserve.teachua.model.Task;
import com.softserve.teachua.repository.TaskRepository;
import com.softserve.teachua.service.ChallengeService;
import com.softserve.teachua.tools.FileUtils;
import com.softserve.teachua.tools.dao.TaskDao;
import com.softserve.teachua.tools.repository.TaskInfoRepository;
import com.softserve.teachua.tools.service.TaskTransferService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskTransferServiceImpl implements TaskTransferService {
    private static final String TARGET_PACKAGE = "tasks";
    private static final String TARGET_PACKAGE_FOR_DATA = "tasks/taskData";
    private final FileUtils fileUtils;
    private final DtoConverter dtoConverter;
    private final TaskRepository taskRepository;
    private final ChallengeService challengeService;
    private final TaskDao taskDao;
    private static final String UA_TEACH_CHALLENGE = "Навчай українською челендж"; // id=1
    private static final String LANGUAGE_CHALLENGE = "Мовомаратон"; // id=2
    private final Map<String, List<String>> imageMap;
    private String uaTeachChallengeImage = "data_for_db/task_images/challengeUA.jpg";
    private String languageChallengeImage = "data_for_db/task_images/marathon_log.png";

    @Autowired
    public TaskTransferServiceImpl(FileUtils fileReader, DtoConverter dtoConverter, TaskRepository taskRepository,
                                   ChallengeService challengeService, TaskDao taskDao) {
        this.fileUtils = fileReader;
        this.dtoConverter = dtoConverter;
        this.taskRepository = taskRepository;
        this.challengeService = challengeService;
        this.taskDao = taskDao;
        imageMap = new HashMap<>();
        imageMap.put("Крок 16. Рахуйте українською ",
                Arrays.asList("data_for_db/taskData/day16_1.png", "data_for_db/taskData/day16_2.png",
                        "data_for_db/taskData/day16_3.png", "data_for_db/taskData/day16_4.png"));
        imageMap.put("Крок 17. Мандруйте Україною ", List.of("data_for_db/taskData/day17_1.png"));
        imageMap.put("Крок 24. Пізнавайте українське мистецтво", List.of("data_for_db/taskData/day24_1.jpg"));
    }

    @Override
    public List<SuccessCreatedTask> createTasksFromFile(String filePath) {
        moveImages();
        return createTasks(fileUtils.readFromFile(filePath, TaskProfile.class));
    }

    @Override
    public List<SuccessCreatedTask> createTasksFromRepository() {
        return createTasks(TaskInfoRepository.getTasksList());
    }

    private void moveImages() {
        uaTeachChallengeImage = fileUtils.moveImage(uaTeachChallengeImage, TARGET_PACKAGE);
        languageChallengeImage = fileUtils.moveImage(languageChallengeImage, TARGET_PACKAGE);
    }

    private List<SuccessCreatedTask> createTasks(List<TaskProfile> tasks) {
        return tasks.stream().map(taskProfile -> {
            if (taskProfile.getChallengeId() == 1) {
                taskProfile.setChallengeId(challengeService.getChallengeByName(UA_TEACH_CHALLENGE).getId());
                taskProfile.setPicture(uaTeachChallengeImage);
            } else {
                taskProfile.setChallengeId(challengeService.getChallengeByName(LANGUAGE_CHALLENGE).getId());
                taskProfile.setPicture(languageChallengeImage);
            }

            if (imageMap.containsKey(taskProfile.getName())) {
                imageMap.get(taskProfile.getName()).stream()
                        .forEach(url -> taskProfile.setDescription(taskProfile.getDescription().replace(url,
                                System.getenv("BASE_URL") + fileUtils.moveImage(url, TARGET_PACKAGE_FOR_DATA))));
            }

            log.info("add task: " + taskProfile);
            return dtoConverter.convertToEntity(taskProfile, Task.builder().build()).withId(null);
        }).map(taskRepository::save).map(task -> {
            log.info("Task: " + task);
            return (SuccessCreatedTask) dtoConverter.convertToDto(task, SuccessCreatedTask.class);
        }).toList();
    }

    @Override
    public void dropRedundantTable() {
        taskDao.dropRedundantChallenges();
    }

    @Override
    public void addConstrain() {
        taskDao.alterTaskConstrain();
    }

    @Override
    public void dropConstrain() {
        taskDao.alterTaskConstrain();
    }
}
