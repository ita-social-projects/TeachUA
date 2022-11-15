package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.log.LogResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.service.LogService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LogServiceImpl implements LogService {
    @Value(value = "${logs.path}")
    private String path;
    private static final String CREATE_MESSAGE = "Created sub directory in logs with name: %s";
    private static final String FILE_NAME = "teachualogs";

    @Override
    public List<String> getAllLogs() {
        List<String> result = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            result = walk
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(f -> f.startsWith(FILE_NAME))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        } catch (IOException e) {
            log.info("Error occurred while walking through log files in directory: {}", path, e);
        }

        return result;
    }

    @Override
    public List<String> getLogByName(String fileName) {
        List<String> result = new ArrayList<>();

        Path pathToFile = Paths.get(path, fileName);

        if (!Files.exists(pathToFile)) {
            log.info("Tried deleting file: {}, but it does not exist", fileName);
            return result;
        }

        try (Stream<String> lines = Files.lines(pathToFile)) {
            result = lines.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void deleteLogByName(String fileName) {
        Path pathToFile = Paths.get(path, fileName);

        if (!Files.exists(pathToFile)) {
            log.info("File {} does not exist", fileName);
        }

        try {
            Files.delete(Paths.get(path, fileName));
        } catch (IOException e) {
            log.error("Error occurred while deleting file: {}", fileName, e);
        }
    }

    @Override
    public List<String> getAbsolutePathForLogs() {

        List<String> pathList = new LinkedList<>();
        FileUtils.listFiles(new File(path), null, false).stream().forEach(file -> {
            if (!file.getName().contains("catalina")) {
                pathList.add(file.getAbsolutePath());
            }
        });

        return pathList;
    }

    @Override
    public String createSubDirectoryByName(String name) throws IOException {

        File file = null;
        if (!name.matches("[a-zA-Z-_\\d]+")) {
            throw new IncorrectInputException("File name should contain only english letters,digit and char like -_");
        }

        if (name.matches("date")) {
            file = new File((path + LocalDate.now()).replace(" ", ""));
            if (file.exists()) {
                throw new AlreadyExistException("Directory with this name already exist");
            }
            FileUtils.forceMkdir(file);
        } else {
            file = new File((path + name).replace(" ", ""));
            if (file.exists()) {
                throw new AlreadyExistException("Directory with this name already exist");
            }
            FileUtils.forceMkdir(file);
        }
        return String.format(CREATE_MESSAGE, file.getName());
    }

    @Override
    public LogResponse moveLogsToSubDirectoryByDirectoryName(String directoryName) {

        List<String> movedFileList = new LinkedList<>();
        List<String> notMovedFileList = new LinkedList<>();

        File fileMoveTo = new File((path + directoryName).replace(" ", ""));

        if (fileMoveTo.exists() && fileMoveTo.isDirectory()) {
            FileUtils.listFiles(new File(path), null, false).forEach(file -> {

                File fileMoveFrom = new File((path + file.getName()).replace(" ", ""));

                System.out.println(fileMoveFrom);
                if (!fileMoveFrom.isDirectory()) {
                    try {
                        FileUtils.moveFileToDirectory(fileMoveFrom, fileMoveTo, false);
                        movedFileList.add("Moved file from:" + fileMoveFrom.getAbsolutePath());
                        movedFileList.add("To directory " + fileMoveTo.getAbsolutePath());
                    } catch (IOException e) {
                        notMovedFileList.add("Not moved file: " + fileMoveFrom.getName());
                    }
                }
            });
        } else {
            throw new IncorrectInputException("Directory by name: " + directoryName + " doesnt exist");
        }

        return new LogResponse().withDeletedLogs(movedFileList).withNotDeletedLogs(notMovedFileList);
    }

    @Override
    public LogResponse deleteEmptyLogs(Boolean filter) {

        List<String> deletedLogs = new LinkedList<>();
        List<String> notDeletedLogs = new LinkedList<>();

        if (filter) {
            FileUtils.listFiles(new File(path), null, false).forEach(file -> {
                String fileName = (path + file.getName()).replace(" ", "");
                File checkedFile = FileUtils.getFile(fileName);
                try {
                    if (checkedFile.length() < 1 && !checkedFile.isDirectory()) {
                        FileUtils.forceDelete(checkedFile);
                        deletedLogs.add(file.getName());
                    }
                } catch (IOException e) {
                    notDeletedLogs.add("NOT deleted file:" + file.getName());
                }
            });
        }

        return new LogResponse().withDeletedLogs(deletedLogs).withNotDeletedLogs(notDeletedLogs);
    }

}
