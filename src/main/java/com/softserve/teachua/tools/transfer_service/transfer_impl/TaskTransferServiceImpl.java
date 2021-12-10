package com.softserve.teachua.tools.transfer_service.transfer_impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.task.SuccessCreatedTask;
import com.softserve.teachua.dto.task.TaskProfile;
import com.softserve.teachua.tools.FileReader;
import com.softserve.teachua.tools.transfer_service.TaskTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskTransferServiceImpl implements TaskTransferService {

    private final FileReader fileReader;
    private final DtoConverter dtoConverter;

    @Autowired
    public TaskTransferServiceImpl(FileReader fileReader, DtoConverter dtoConverter) {
        this.fileReader = fileReader;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<SuccessCreatedTask> createTasksFromFile(String filePath) {
        List<TaskProfile> tasks = fileReader.readFromFile(filePath, TaskProfile.class);
        return tasks.stream()
                .map(task -> dtoConverter.convertFromDtoToDto(task, SuccessCreatedTask.builder().build()))
                .collect(Collectors.toList());
    }
}
