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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskTransferServiceImpl implements TaskTransferService {
    private final FileUtils fileUtils;
    private final DtoConverter dtoConverter;
    private final TaskRepository taskRepository;
    private final ChallengeService challengeService;
    private final TaskDao taskDao;

    private final String uaTeachChallenge = "Навчай українською челендж"; // id=1
    private final String languageChallenge = "Мовомаратон"; // id=2
    private String uaTeachChallengeImage = "data_for_db/task_images/challengeUA.jpg";
    private String languageChallengeImage = "data_for_db/task_images/marathon_log.png";
    private static final String targetPackage = "tasks";
    private static final String targetPackageForData = "tasks/taskData";
    private final Map imageMap;


    @Autowired
    public TaskTransferServiceImpl(
            FileUtils fileReader,
            DtoConverter dtoConverter,
            TaskRepository taskRepository, ChallengeService challengeService, TaskDao taskDao) {
        this.fileUtils = fileReader;
        this.dtoConverter = dtoConverter;
        this.taskRepository = taskRepository;
        this.challengeService = challengeService;
        this.taskDao = taskDao;
        imageMap = new HashMap<String, List<String>>();
        imageMap.put("Крок 16. Рахуйте українською ",
                Arrays.asList(
                        "data_for_db/taskData/day16_1.png",
                        "data_for_db/taskData/day16_2.png",
                        "data_for_db/taskData/day16_3.png",
                        "data_for_db/taskData/day16_4.png"
                ));
        imageMap.put("Крок 17. Мандруйте Україною ", Arrays.asList("data_for_db/taskData/day17_1.png"));
        imageMap.put("Крок 24. Пізнавайте українське мистецтво", Arrays.asList("data_for_db/taskData/day24_1.jpg"));
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

    private void moveImages(){
        uaTeachChallengeImage = fileUtils.moveImage(uaTeachChallengeImage, targetPackage);
        languageChallengeImage = fileUtils.moveImage(languageChallengeImage, targetPackage);
    }

    private List<SuccessCreatedTask> createTasks(List<TaskProfile> tasks) {
        return tasks
                .stream()
                .map(taskProfile -> {
//                    if(!taskProfile.getPicture().isEmpty()) {
//                        taskProfile.setPicture(fileUtils.moveImage(taskProfile.getPicture(), "tasks"));
//                    }
//                    temporary implementation (uses only once)
                    if(taskProfile.getChallengeId() == 1){
                        taskProfile.setChallengeId(challengeService.getChallengeByName(uaTeachChallenge).getId());
                        taskProfile.setPicture(uaTeachChallengeImage);
                    }else{
                        taskProfile.setChallengeId(challengeService.getChallengeByName(languageChallenge).getId());
                        taskProfile.setPicture(languageChallengeImage);
                    }

                    if(imageMap.containsKey(taskProfile.getName())){
                        ((List<String>)imageMap.get(taskProfile.getName()))
                                .stream()
                                .forEach(url ->
                                    taskProfile.setDescription(
                                            taskProfile
                                            .getDescription()
                                            .replace(url,
                                                    System.getenv("BASE_URL")
                                                            + fileUtils.moveImage(url, targetPackageForData))
                                    )
                                );
                    }

                    log.info("add task: " + taskProfile);
                    return dtoConverter.convertToEntity(taskProfile, Task.builder().build()).withId(null);
                }).map(taskRepository::save)
                .map(task -> {
                    log.info("Task: " + task);
                    return (SuccessCreatedTask) dtoConverter.convertToDto(task, SuccessCreatedTask.class);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void dropRedundantTable(){
        taskDao.dropRedundantChallenges();
    }

    @Override
    public void addConstrain(){
        taskDao.alterTaskConstrain();
    }

    @Override
    public void dropConstrain(){
        taskDao.alterTaskConstrain();
    }

}
